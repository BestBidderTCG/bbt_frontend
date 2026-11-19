package com.gps_g6.bestbiddertcg.core.network

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.auth.SessionTokens
import com.gps_g6.bestbiddertcg.core.auth.TokenStorage
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.LoginResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TokenAuthenticatorTest {

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

    private lateinit var sessionManager: SessionManager
    private lateinit var refreshApi: AuthApi
    private lateinit var authenticator: TokenAuthenticator

    @Before
    fun setUp() {
        sessionManager = SessionManager(FakeTokenStorage())
        sessionManager.saveSession(
            accessToken = "old-access",
            refreshToken = "old-refresh"
        )

        refreshApi = mockk()
        authenticator = TokenAuthenticator(
            sessionManager = sessionManager,
            refreshApi = refreshApi
        )
    }

    private fun request(
        path: String = "/api/users/me",
        authorization: String? = "Bearer old-access"
    ): Request {
        return Request.Builder()
            .url("http://localhost:8080$path")
            .apply {
                if (authorization != null) {
                    header("Authorization", authorization)
                }
            }
            .build()
    }

    private fun unauthorizedResponse(
        request: Request = request(),
        priorResponse: Response? = null
    ): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .apply {
                if (priorResponse != null) {
                    priorResponse(priorResponse)
                }
            }
            .build()
    }

    @Test
    fun `successful refresh retries with new access token`() {
        coEvery {
            refreshApi.refresh(
                match { it.refreshToken == "old-refresh" }
            )
        } returns LoginResponseDto(
            accessToken = "new-access",
            refreshToken = "new-refresh"
        )

        val retry = authenticator.authenticate(
            null,
            unauthorizedResponse()
        )

        assertNotNull(retry)
        assertEquals(
            "Bearer new-access",
            retry?.header("Authorization")
        )
        assertEquals("new-access", sessionManager.getAccessToken())
        assertEquals("new-refresh", sessionManager.getRefreshToken())

        coVerify(exactly = 1) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `failed refresh does not retry request`() {
        coEvery {
            refreshApi.refresh(any())
        } throws IOException("Connection failed")

        val retry = authenticator.authenticate(
            null,
            unauthorizedResponse()
        )

        assertNull(retry)
        assertEquals("old-access", sessionManager.getAccessToken())
        assertEquals("old-refresh", sessionManager.getRefreshToken())

        coVerify(exactly = 1) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `logout during refresh cannot restore session`() {
        coEvery {
            refreshApi.refresh(any())
        } coAnswers {
            // Simulate logout while refresh is in progress
            sessionManager.clearSession()

            LoginResponseDto(
                accessToken = "new-access",
                refreshToken = "new-refresh"
            )
        }

        val retry = authenticator.authenticate(
            null,
            unauthorizedResponse()
        )

        assertNull(retry)
        assertFalse(sessionManager.isAuthenticated())
        assertNull(sessionManager.getAccessToken())
        assertNull(sessionManager.getRefreshToken())
    }

    @Test
    fun `already renewed session reuses current access token`() {
        val originalRequest = request()

        // Simulate another request completing refresh first
        sessionManager.saveSession(
            accessToken = "new-access",
            refreshToken = "new-refresh"
        )

        val retry = authenticator.authenticate(
            null,
            unauthorizedResponse(originalRequest)
        )

        assertNotNull(retry)
        assertEquals(
            "Bearer new-access",
            retry?.header("Authorization")
        )

        coVerify(exactly = 0) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `authentication endpoint does not trigger refresh`() {
        val response = unauthorizedResponse(
            request(path = "/api/auth/login")
        )

        val retry = authenticator.authenticate(null, response)

        assertNull(retry)

        coVerify(exactly = 0) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `request without authorization does not trigger refresh`() {
        val response = unauthorizedResponse(
            request(authorization = null)
        )

        val retry = authenticator.authenticate(null, response)

        assertNull(retry)

        coVerify(exactly = 0) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `already retried request does not refresh again`() {
        val originalResponse = unauthorizedResponse()

        val response = unauthorizedResponse(
            request = request(),
            priorResponse = originalResponse
        )

        val retry = authenticator.authenticate(null, response)

        assertNull(retry)

        coVerify(exactly = 0) {
            refreshApi.refresh(any())
        }
    }

    @Test
    fun `non bearer authorization does not trigger refresh`() {
        val response = unauthorizedResponse(
            request(authorization = "Basic example")
        )

        val retry = authenticator.authenticate(null, response)

        assertNull(retry)

        coVerify(exactly = 0) {
            refreshApi.refresh(any())
        }
    }

}
