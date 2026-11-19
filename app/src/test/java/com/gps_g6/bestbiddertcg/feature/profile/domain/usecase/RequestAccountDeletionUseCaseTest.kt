package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.AccountDeletionStatus
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RequestAccountDeletionUseCaseTest {

    private val repository = mockk<ProfileRepository>()
    private val sessionManager = mockk<SessionManager>()
    private val profileManager = mockk<ProfileManager>()

    private val useCase = RequestAccountDeletionUseCase(
        repository = repository,
        sessionManager = sessionManager,
        profileManager = profileManager
    )

    private fun httpError(code: Int): HttpException {
        val body = "{}".toResponseBody(
            "application/json".toMediaType()
        )
        return HttpException(Response.error<Any>(code, body))
    }

    @Test
    fun `accepted deletion clears session and profile`() = runBlocking {
        coEvery {
            repository.requestAccountDeletion()
        } returns AccountDeletionStatus(
            status = "PENDING_DELETION",
            deletionRequestedAt = null,
            scheduledPurgeAt = null
        )

        every { sessionManager.clearSession() } just Runs
        coEvery { profileManager.clearProfile() } just Runs

        useCase()

        coVerify(exactly = 1) {
            repository.requestAccountDeletion()
        }

        verify(exactly = 1) {
            sessionManager.clearSession()
        }

        coVerify(exactly = 1) {
            profileManager.clearProfile()
        }
    }

    @Test
    fun `conflict does not clear local session`() = runBlocking {
        coEvery {
            repository.requestAccountDeletion()
        } throws httpError(409)

        try {
            useCase()
            fail("Expected HTTP 409")
        } catch (e: HttpException) {
            assertEquals(409, e.code())
        }

        verify(exactly = 0) {
            sessionManager.clearSession()
        }

        coVerify(exactly = 0) {
            profileManager.clearProfile()
        }
    }

    @Test
    fun `network error does not clear local session`() = runBlocking {
        coEvery {
            repository.requestAccountDeletion()
        } throws IOException("Connection lost")

        try {
            useCase()
            fail("Expected IOException")
        } catch (_: IOException) {
            // The ViewModel handles the uncertainty.
        }

        verify(exactly = 0) {
            sessionManager.clearSession()
        }

        coVerify(exactly = 0) {
            profileManager.clearProfile()
        }
    }

    @Test
    fun `unexpected deletion status does not clear session`() = runBlocking {
        coEvery {
            repository.requestAccountDeletion()
        } returns AccountDeletionStatus(
            status = "ACTIVE",
            deletionRequestedAt = null,
            scheduledPurgeAt = null
        )

        try {
            useCase()
            fail("Expected IllegalStateException")
        } catch (_: IllegalStateException) {
            // An unexpected response must not count as success.
        }

        verify(exactly = 0) {
            sessionManager.clearSession()
        }

        coVerify(exactly = 0) {
            profileManager.clearProfile()
        }
    }
}
