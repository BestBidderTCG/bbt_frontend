package com.gps_g6.bestbiddertcg.feature.auth.presentation

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
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
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuthRepository
    private lateinit var sessionManager: SessionManager

    private var saveProfileCalls = 0
    private var savedDescription: String? = null
    private var savedAvatar: Int? = null

    private var profileShouldFail = false

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        sessionManager = mockk(relaxed = true)

        saveProfileCalls = 0
        savedDescription = null
        savedAvatar = null
        profileShouldFail = false

        // Successful registration response.
        coEvery {
            repository.register(any(), any(), any())
        } returns mockk(relaxed = true)

        // Successful login response with test tokens.
        coEvery {
            repository.login(any(), any())
        } returns mockk {
            every { accessToken } returns "test-access-token"
            every { refreshToken } returns "test-refresh-token"
        }

        viewModel = RegisterViewModel(
            repository = repository,
            sessionManager = sessionManager,
            saveProfile = { description, avatarNum ->
                saveProfileCalls++

                if (profileShouldFail) {
                    throw IOException("Profile update failed")
                }

                savedDescription = description
                savedAvatar = avatarNum
            },
            ioDispatcher = testDispatcher
        )

        // Valid registration data.
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("Test123456")
        viewModel.onConfirmPasswordChange("Test123456")
        viewModel.onUsernameChange("testuser")
        viewModel.onDescriptionChange("Colecciono cartas TCG")
        viewModel.onAvatarSelected(4)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful registration saves profile`() = runTest(
        testDispatcher
    ) {
        viewModel.registerAndLogin()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.accountCreated)
        assertTrue(state.loginCompleted)
        assertTrue(state.isRegistered)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

        coVerify(exactly = 1) {
            repository.register(
                "test@example.com",
                "testuser",
                "Test123456"
            )
        }

        coVerify(exactly = 1) {
            repository.login(
                "test@example.com",
                "Test123456"
            )
        }

        verify(exactly = 1) {
            sessionManager.saveSession(
                "test-access-token",
                "test-refresh-token"
            )
        }

        assertEquals(1, saveProfileCalls)
        assertEquals(
            "Colecciono cartas TCG",
            savedDescription
        )
        assertEquals(4, savedAvatar)
    }

    @Test
    fun `registration failure does not attempt login or profile update`() =
        runTest(testDispatcher) {

            coEvery {
                repository.register(any(), any(), any())
            } throws IOException("Registration failed")

            viewModel.registerAndLogin()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertFalse(state.accountCreated)
            assertFalse(state.loginCompleted)
            assertFalse(state.isRegistered)
            assertFalse(state.isLoading)
            assertNotNull(state.errorMessage)

            coVerify(exactly = 1) {
                repository.register(any(), any(), any())
            }

            coVerify(exactly = 0) {
                repository.login(any(), any())
            }

            assertEquals(0, saveProfileCalls)
        }

    @Test
    fun `login failure can retry without registering again`() =
        runTest(testDispatcher) {

            coEvery {
                repository.login(any(), any())
            } throws IOException("Login failed")

            // First attempt: register succeeds, login fails.
            viewModel.registerAndLogin()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.accountCreated)
            assertFalse(viewModel.uiState.value.loginCompleted)
            assertFalse(viewModel.uiState.value.isRegistered)

            // Restore successful login behavior.
            coEvery {
                repository.login(any(), any())
            } returns mockk {
                every { accessToken } returns "test-access-token"
                every { refreshToken } returns "test-refresh-token"
            }

            // Second attempt: only login and profile update.
            viewModel.registerAndLogin()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isRegistered)

            coVerify(exactly = 1) {
                repository.register(any(), any(), any())
            }

            coVerify(exactly = 2) {
                repository.login(any(), any())
            }

            assertEquals(1, saveProfileCalls)
        }

    @Test
    fun `profile failure retries only profile update`() =
        runTest(testDispatcher) {

            profileShouldFail = true

            // First attempt: register and login succeed,
            // but saving the profile fails.
            viewModel.registerAndLogin()
            advanceUntilIdle()

            val failedState = viewModel.uiState.value

            assertTrue(failedState.accountCreated)
            assertTrue(failedState.loginCompleted)
            assertFalse(failedState.isRegistered)
            assertNotNull(failedState.errorMessage)

            // Second attempt: profile update succeeds.
            profileShouldFail = false

            viewModel.registerAndLogin()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isRegistered)

            coVerify(exactly = 1) {
                repository.register(any(), any(), any())
            }

            coVerify(exactly = 1) {
                repository.login(any(), any())
            }

            assertEquals(2, saveProfileCalls)
            assertEquals(4, savedAvatar)
        }

    @Test
    fun `completed registration cannot be submitted twice`() =
        runTest(testDispatcher) {

            viewModel.registerAndLogin()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isRegistered)

            viewModel.registerAndLogin()
            advanceUntilIdle()

            coVerify(exactly = 1) {
                repository.register(any(), any(), any())
            }

            coVerify(exactly = 1) {
                repository.login(any(), any())
            }

            assertEquals(1, saveProfileCalls)
        }
}
