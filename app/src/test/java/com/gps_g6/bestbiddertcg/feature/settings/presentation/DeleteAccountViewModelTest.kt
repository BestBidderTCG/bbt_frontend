package com.gps_g6.bestbiddertcg.feature.settings.presentation

import com.gps_g6.bestbiddertcg.feature.profile.domain.model.AccountDeletionCheck
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.CheckAccountDeletionUseCase
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.RequestAccountDeletionUseCase
import io.mockk.*
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
class DeleteAccountViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var checkUseCase: CheckAccountDeletionUseCase
    private lateinit var requestUseCase: RequestAccountDeletionUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        checkUseCase = mockk()
        requestUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun httpError(code: Int): HttpException {
        val body = "{}".toResponseBody(
            "application/json".toMediaType()
        )
        return HttpException(Response.error<Any>(code, body))
    }

    private fun allowedCheck() = AccountDeletionCheck(
        hasActiveAuction = false,
        isWinningAuction = false
    )

    @Test
    fun `account without blockers becomes allowed`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns allowedCheck()

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        assertEquals(
            DeleteAccountUiState.Allowed,
            viewModel.uiState.value.screen
        )

        assertNull(viewModel.uiState.value.error)

        coVerify(exactly = 1) {
            checkUseCase.invoke()
        }
    }

    @Test
    fun `account with active auction becomes blocked`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns AccountDeletionCheck(
            hasActiveAuction = true,
            isWinningAuction = false
        )

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        assertEquals(
            DeleteAccountUiState.Blocked(
                hasActiveAuction = true,
                isWinningAuction = false
            ),
            viewModel.uiState.value.screen
        )
    }

    @Test
    fun `account winning auction becomes blocked`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns AccountDeletionCheck(
            hasActiveAuction = false,
            isWinningAuction = true
        )

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        assertEquals(
            DeleteAccountUiState.Blocked(
                hasActiveAuction = false,
                isWinningAuction = true
            ),
            viewModel.uiState.value.screen
        )
    }

    @Test
    fun `check error shows retry state`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } throws IOException("Server unavailable")

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        assertEquals(
            DeleteAccountUiState.Error,
            viewModel.uiState.value.screen
        )

        assertEquals(
            DeleteAccountError.CHECK_FAILED,
            viewModel.uiState.value.error
        )
    }

    @Test
    fun `successful deletion marks request as completed`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns allowedCheck()

        coEvery {
            requestUseCase.invoke()
        } just Runs

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        viewModel.requestDeletion()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.requestSucceeded)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertNull(viewModel.uiState.value.error)

        coVerify(exactly = 1) {
            requestUseCase.invoke()
        }
    }

    @Test
    fun `blocked account cannot submit deletion`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns AccountDeletionCheck(
            hasActiveAuction = true,
            isWinningAuction = false
        )

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        viewModel.requestDeletion()
        advanceUntilIdle()

        coVerify(exactly = 0) {
            requestUseCase.invoke()
        }
    }

    @Test
    fun `conflict refreshes blockers`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returnsMany listOf(
            allowedCheck(),
            AccountDeletionCheck(
                hasActiveAuction = true,
                isWinningAuction = false
            )
        )

        coEvery {
            requestUseCase.invoke()
        } throws httpError(409)

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        viewModel.requestDeletion()
        advanceUntilIdle()

        assertEquals(
            DeleteAccountUiState.Blocked(
                hasActiveAuction = true,
                isWinningAuction = false
            ),
            viewModel.uiState.value.screen
        )

        assertFalse(viewModel.uiState.value.requestSucceeded)

        coVerify(exactly = 2) {
            checkUseCase.invoke()
        }
    }

    @Test
    fun `network error keeps request unsuccessful`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns allowedCheck()

        coEvery {
            requestUseCase.invoke()
        } throws IOException("Connection lost")

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        viewModel.requestDeletion()
        advanceUntilIdle()

        assertEquals(
            DeleteAccountError.REQUEST_UNCERTAIN,
            viewModel.uiState.value.error
        )

        assertFalse(viewModel.uiState.value.requestSucceeded)
        assertFalse(viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `repeated deletion requests only submit once`() = runTest {
        coEvery {
            checkUseCase.invoke()
        } returns allowedCheck()

        coEvery {
            requestUseCase.invoke()
        } just Runs

        val viewModel = DeleteAccountViewModel(
            checkUseCase,
            requestUseCase
        )

        advanceUntilIdle()

        viewModel.requestDeletion()
        viewModel.requestDeletion()

        advanceUntilIdle()

        coVerify(exactly = 1) {
            requestUseCase.invoke()
        }

        assertTrue(viewModel.uiState.value.requestSucceeded)
    }
}
