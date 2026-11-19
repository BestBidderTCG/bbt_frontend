package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.auth.SessionTokens
import com.gps_g6.bestbiddertcg.core.auth.TokenStorage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RestoreSessionUseCaseTest {

    private class FakeTokenStorage : TokenStorage {

        private var tokens: SessionTokens? = null

        override fun saveTokens(tokens: SessionTokens) {
            this.tokens = tokens
        }

        override fun getTokens(): SessionTokens? = tokens

        override fun clearTokens() {
            tokens = null
        }
    }

    private fun httpError(code: Int): HttpException {
        val body = "{}".toResponseBody(
            "application/json".toMediaType()
        )
        return HttpException(Response.error<Any>(code, body))
    }

    @Test
    fun `restore without stored session returns false`() = runBlocking {
        val manager = SessionManager(FakeTokenStorage())
        val refreshSession = mockk<RefreshSessionUseCase>()

        val result = RestoreSessionUseCase(
            sessionManager = manager,
            refreshSession = refreshSession
        )()

        assertFalse(result)
        assertFalse(manager.isAuthenticated())

        coVerify(exactly = 0) {
            refreshSession.invoke()
        }
    }

    @Test
    fun `valid stored session is restored and refreshed`() = runBlocking {
        val storage = FakeTokenStorage()

        // Simulate a previous execution of the application.
        SessionManager(storage).saveSession(
            accessToken = "old-access",
            refreshToken = "old-refresh"
        )

        val manager = SessionManager(storage)
        val refreshSession = mockk<RefreshSessionUseCase>()

        coEvery {
            refreshSession.invoke()
        } coAnswers {
            manager.saveSession(
                accessToken = "new-access",
                refreshToken = "new-refresh"
            )
            true
        }

        val result = RestoreSessionUseCase(
            sessionManager = manager,
            refreshSession = refreshSession
        )()

        assertTrue(result)
        assertTrue(manager.isAuthenticated())
        assertEquals("new-access", manager.getAccessToken())
        assertEquals("new-refresh", manager.getRefreshToken())

        coVerify(exactly = 1) {
            refreshSession.invoke()
        }
    }

    @Test
    fun `rejected refresh token clears stored session`() = runBlocking {
        for (code in listOf(400, 401, 403)) {
            val storage = FakeTokenStorage()

            SessionManager(storage).saveSession(
                accessToken = "old-access",
                refreshToken = "old-refresh"
            )

            val manager = SessionManager(storage)
            val refreshSession = mockk<RefreshSessionUseCase>()

            coEvery {
                refreshSession.invoke()
            } throws httpError(code)

            val result = RestoreSessionUseCase(
                sessionManager = manager,
                refreshSession = refreshSession
            )()

            assertFalse(result)
            assertFalse(manager.isAuthenticated())
            assertNull(manager.getAccessToken())
            assertNull(manager.getRefreshToken())
            assertNull(storage.getTokens())
        }
    }

    @Test
    fun `server error preserves session and propagates error`() =
        runBlocking {
            val storage = FakeTokenStorage()

            SessionManager(storage).saveSession(
                accessToken = "old-access",
                refreshToken = "old-refresh"
            )

            val manager = SessionManager(storage)
            val refreshSession = mockk<RefreshSessionUseCase>()

            coEvery {
                refreshSession.invoke()
            } throws httpError(500)

            try {
                RestoreSessionUseCase(
                    sessionManager = manager,
                    refreshSession = refreshSession
                )()
                fail("Expected HTTP 500")
            } catch (e: HttpException) {
                assertEquals(500, e.code())
            }

            assertTrue(manager.isAuthenticated())
            assertEquals("old-access", manager.getAccessToken())
            assertEquals("old-refresh", manager.getRefreshToken())
        }

    @Test
    fun `network error preserves session for retry`() = runBlocking {
        val storage = FakeTokenStorage()

        SessionManager(storage).saveSession(
            accessToken = "old-access",
            refreshToken = "old-refresh"
        )

        val manager = SessionManager(storage)
        val refreshSession = mockk<RefreshSessionUseCase>()

        coEvery {
            refreshSession.invoke()
        } throws IOException("Network unavailable")

        try {
            RestoreSessionUseCase(
                sessionManager = manager,
                refreshSession = refreshSession
            )()
            fail("Expected IOException")
        } catch (_: IOException) {
            // Expected: StartupViewModel handles this error.
        }

        assertTrue(manager.isAuthenticated())
        assertEquals("old-refresh", manager.getRefreshToken())
        assertNotNull(storage.getTokens())
    }
}
