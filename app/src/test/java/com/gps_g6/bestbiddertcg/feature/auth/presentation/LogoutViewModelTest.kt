package com.gps_g6.bestbiddertcg.feature.auth.presentation

import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.LogoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: LogoutViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        logoutUseCase = mockk()

        coEvery {
            logoutUseCase.invoke()
        } just Runs

        viewModel = LogoutViewModel(logoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful logout updates state`() =
        runTest(testDispatcher) {

            viewModel.logout()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(state.isLoggedOut)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)

            coVerify(exactly = 1) {
                logoutUseCase.invoke()
            }
        }

    @Test
    fun `logout failure shows error`() =
        runTest(testDispatcher) {

            coEvery {
                logoutUseCase.invoke()
            } throws IOException("Logout failed")

            viewModel.logout()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertFalse(state.isLoggedOut)
            assertFalse(state.isLoading)
            assertEquals(
                "No se ha podido cerrar la sesión.",
                state.errorMessage
            )
        }

    @Test
    fun `logout cannot be repeated after success`() =
        runTest(testDispatcher) {

            viewModel.logout()
            advanceUntilIdle()

            viewModel.logout()
            advanceUntilIdle()

            coVerify(exactly = 1) {
                logoutUseCase.invoke()
            }
        }

    @Test
    fun `logout enters loading state`() =
        runTest(testDispatcher) {

            viewModel.logout()

            // Execute the ViewModel coroutine
            runCurrent()

            // With the immediate mock response the operation has
            // already finished, so check the final state
            assertTrue(viewModel.uiState.value.isLoggedOut)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `logout cannot be submitted twice while loading`() =
        runTest(testDispatcher) {

            val logoutGate = CompletableDeferred<Unit>()

            coEvery {
                logoutUseCase.invoke()
            } coAnswers {
                logoutGate.await()
                Unit
            }

            // Start the first logout
            viewModel.logout()
            runCurrent()

            assertTrue(viewModel.uiState.value.isLoading)
            assertFalse(viewModel.uiState.value.isLoggedOut)

            // Try again while the first logout is pending
            viewModel.logout()
            runCurrent()

            coVerify(exactly = 1) {
                logoutUseCase.invoke()
            }

            // Allow the first operation to finish
            logoutGate.complete(Unit)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isLoggedOut)
            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.errorMessage)

            coVerify(exactly = 1) {
                logoutUseCase.invoke()
            }
        }

}
