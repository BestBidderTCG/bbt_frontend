package com.gps_g6.bestbiddertcg.core.network

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.RefreshRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val sessionManager: SessionManager,
    private val refreshApi: AuthApi
) : Authenticator {

    private val refreshLock = Any()

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Never refresh authentication endpoints.
        if (response.request.url.encodedPath.startsWith("/api/auth/")) {
            return null
        }

        // Only authenticated requests may be retried.
        val previousAuthorization =
            response.request.header("Authorization") ?: return null

        // Avoid repeated authentication attempts.
        if (response.priorResponse != null) {
            return null
        }

        synchronized(refreshLock) {

            val currentAccessToken =
                sessionManager.getAccessToken() ?: return null

            val currentRefreshToken =
                sessionManager.getRefreshToken() ?: return null

            // Another request may have already refreshed the tokens.
            if (previousAuthorization != "Bearer $currentAccessToken") {
                return response.request.newBuilder()
                    .header(
                        "Authorization",
                        "Bearer $currentAccessToken"
                    )
                    .build()
            }

            // Refresh synchronously using a separate HTTP client.
            val refreshedSession = try {
                runBlocking {
                    refreshApi.refresh(
                        RefreshRequestDto(
                            refreshToken = currentRefreshToken
                        )
                    )
                }
            } catch (e: Exception) {
                return null
            }

            // Do not restore a session removed by logout.
            val saved = sessionManager.saveSessionIfRefreshTokenMatches(
                expectedRefreshToken = currentRefreshToken,
                accessToken = refreshedSession.accessToken,
                refreshToken = refreshedSession.refreshToken
            )

            if (!saved) {
                return null
            }

            return response.request.newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${refreshedSession.accessToken}"
                )
                .build()
        }
    }
}
