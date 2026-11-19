package com.gps_g6.bestbiddertcg.feature.auth.presentation

import android.util.Log
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.AuthSession
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuthRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Android Log is unavailable in local JVM tests.
        mockkStatic(Log::class)

        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        repository = mockk()
        sessionManager = mockk(relaxed = true)

        coEvery {
            repository.login(any(), any())
        } returns AuthSession(
            accessToken = "test-access",
            refreshToken = "test-refresh"
        )

        viewModel = LoginViewModel(
            repository = repository,
            sessionManager = sessionManager
        )

        viewModel.onIdentifierChange("test@example.com")
        viewModel.onPasswordChange("Test123456")
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    private fun httpError(code: Int): HttpException {
        val body = "{}".toResponseBody(
            "application/json".toMediaType()
        )

        return HttpException(
            Response.error<Any>(code, body)
        )
    }

    @Test
    fun `successful login saves tokens and authenticates user`() =
        runTest(testDispatcher) {

            viewModel.login()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(state.isAuthenticated)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            assertEquals("", state.password)

            coVerify(exactly = 1) {
                repository.login(
                    "test@example.com",
                    "Test123456"
                )
            }

            verify(exactly = 1) {
                sessionManager.saveSession(
                    "test-access",
                    "test-refresh"
                )
            }
        }

    @Test
    fun `empty credentials do not call repository`() =
        runTest(testDispatcher) {

            viewModel.onIdentifierChange("")
            viewModel.onPasswordChange("")

            viewModel.login()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isAuthenticated)
            assertFalse(viewModel.uiState.value.isLoading)
            assertNotNull(viewModel.uiState.value.errorMessage)

            coVerify(exactly = 0) {
                repository.login(any(), any())
            }
        }

    @Test
    fun `invalid credentials show authentication error`() =
        runTest(testDispatcher) {

            coEvery {
                repository.login(any(), any())
            } throws httpError(401)

            viewModel.login()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isAuthenticated)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(
                "Usuario o contraseña incorrectos.",
                viewModel.uiState.value.errorMessage
            )

            verify(exactly = 0) {
                sessionManager.saveSession(any(), any())
            }
        }

    @Test
    fun `network error does not authenticate user`() =
        runTest(testDispatcher) {

            coEvery {
                repository.login(any(), any())
            } throws IOException("Connection unavailable")

            viewModel.login()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isAuthenticated)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(
                "No se ha podido conectar con el servidor.",
                viewModel.uiState.value.errorMessage
            )

            verify(exactly = 0) {
                sessionManager.saveSession(any(), any())
            }
        }

    @Test
    fun `too many attempts show rate limit error`() =
        runTest(testDispatcher) {

            coEvery {
                repository.login(any(), any())
            } throws httpError(429)

            viewModel.login()
            advanceUntilIdle()

            assertEquals(
                "Demasiados intentos. Inténtalo más tarde.",
                viewModel.uiState.value.errorMessage
            )

            assertFalse(viewModel.uiState.value.isAuthenticated)
        }

    @Test
    fun `changing credentials clears previous error`() =
        runTest(testDispatcher) {

            viewModel.onPasswordChange("")
            viewModel.login()

            assertNotNull(viewModel.uiState.value.errorMessage)

            viewModel.onPasswordChange("NewPassword")

            assertNull(viewModel.uiState.value.errorMessage)

            viewModel.onIdentifierChange("another@example.com")

            assertNull(viewModel.uiState.value.errorMessage)
        }

    @Test
    fun `repeated login attempts only submit once`() =
        runTest(testDispatcher) {

            viewModel.login()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isAuthenticated)

            viewModel.login()
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.login(any(), any())
            }

            verify(exactly = 1) {
                sessionManager.saveSession(any(), any())
            }
        }

    @Test
    fun `login cannot be submitted twice while loading`() =
        runTest(testDispatcher) {

            val loginGate = CompletableDeferred<Unit>()

            coEvery {
                repository.login(any(), any())
            } coAnswers {
                loginGate.await()
                AuthSession(
                    accessToken = "test-access",
                    refreshToken = "test-refresh"
                )
            }

            // Start the first login
            viewModel.login()
            runCurrent()

            assertTrue(viewModel.uiState.value.isLoading)

            // Try another login while the first is pending
            viewModel.login()
            runCurrent()

            // Only the first request should reach the repository
            coVerify(exactly = 1) {
                repository.login(any(), any())
            }

            // Complete the pending request
            loginGate.complete(Unit)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isAuthenticated)
            assertFalse(viewModel.uiState.value.isLoading)

            coVerify(exactly = 1) {
                repository.login(any(), any())
            }

            verify(exactly = 1) {
                sessionManager.saveSession(
                    "test-access",
                    "test-refresh"
                )
            }
        }

}
