
package com.gps_g6.bestbiddertcg.core.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(
    private val tokenStorage: TokenStorage
) : AccessTokenProvider {

    private val _session = MutableStateFlow<SessionTokens?>(null)

    val session = _session.asStateFlow()

    override fun getAccessToken(): String? {
        return _session.value?.accessToken
    }

    fun getRefreshToken(): String? {
        return _session.value?.refreshToken
    }

    @Synchronized
    fun saveSession(
        accessToken: String,
        refreshToken: String
    ) {
        require(accessToken.isNotBlank()) {
            "Access token cannot be empty"
        }

        require(refreshToken.isNotBlank()) {
            "Refresh token cannot be empty"
        }

        val tokens = SessionTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        // Persist before publishing the authenticated session.
        tokenStorage.saveTokens(tokens)

        _session.value = tokens
    }

    @Synchronized
    fun restoreSession(): Boolean {
        val tokens = tokenStorage.getTokens()

        if (
            tokens == null ||
            tokens.accessToken.isBlank() ||
            tokens.refreshToken.isBlank()
        ) {
            _session.value = null
            return false
        }

        _session.value = tokens
        return true
    }

    fun isAuthenticated(): Boolean {
        return getAccessToken() != null
    }

    @Synchronized
    fun clearSession() {
        tokenStorage.clearTokens()
        _session.value = null
    }
}
