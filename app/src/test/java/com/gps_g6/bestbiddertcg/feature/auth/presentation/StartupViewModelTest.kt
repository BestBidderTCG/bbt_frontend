package com.gps_g6.bestbiddertcg.feature.auth.presentation

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.RestoreSessionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class StartupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var restoreSession: RestoreSessionUseCase
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        restoreSession = mockk()
        sessionManager = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = StartupViewModel(
        restoreSession = restoreSession,
        sessionManager = sessionManager,
        ioDispatcher = testDispatcher
    )

    private fun httpError(code: Int): HttpException {
        val body = "{}".toResponseBody(
            "application/json".toMediaType()
        )

        return HttpException(
            Response.error<Any>(code, body)
        )
    }

    @Test
    fun `valid restored session authenticates user`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } returns true

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                StartupUiState.Authenticated,
                viewModel.uiState.value
            )

            coVerify(exactly = 1) {
                restoreSession.invoke()
            }
        }

    @Test
    fun `missing session shows unauthenticated state`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } returns false

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                StartupUiState.Unauthenticated,
                viewModel.uiState.value
            )
        }

    @Test
    fun `network error shows connection error`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } throws IOException("Connection unavailable")

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(state is StartupUiState.ConnectionError)

            assertEquals(
                "No se ha podido conectar con el servidor.",
                (state as StartupUiState.ConnectionError).message
            )
        }

    @Test
    fun `server error shows connection error`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } throws httpError(500)

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value is StartupUiState.ConnectionError
            )

            assertEquals(
                "No se ha podido comprobar la sesión.",
                (viewModel.uiState.value as
                        StartupUiState.ConnectionError).message
            )
        }

    @Test
    fun `retry after network failure restores session`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } throws IOException("Network error") andThen true

            val viewModel = createViewModel()

            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value is StartupUiState.ConnectionError
            )

            viewModel.checkSession()
            advanceUntilIdle()

            assertEquals(
                StartupUiState.Authenticated,
                viewModel.uiState.value
            )

            coVerify(exactly = 2) {
                restoreSession.invoke()
            }
        }

    @Test
    fun `discard session clears tokens and continues unauthenticated`() =
        runTest(testDispatcher) {

            coEvery {
                restoreSession.invoke()
            } throws IOException("Network unavailable")

            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.discardSessionAndContinue()
            advanceUntilIdle()

            assertEquals(
                StartupUiState.Unauthenticated,
                viewModel.uiState.value
            )

            verify(exactly = 1) {
                sessionManager.clearSession()
            }
        }
}
