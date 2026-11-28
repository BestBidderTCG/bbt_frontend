package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException

class LogoutUseCase(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val unregisterDevice: suspend () -> Unit = {}
) {

    suspend operator fun invoke() {
        val refreshToken = sessionManager.getRefreshToken()

        try {
            // Unregister FCM before revoking authentication.
            try {
                if (sessionManager.isAuthenticated()) {
                    unregisterDevice()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // FCM failure must not prevent local logout.
            }

            // Revoke the authentication session.
            try {
                if (!refreshToken.isNullOrBlank()) {
                    repository.logout(refreshToken)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Remote revocation may fail.
            }
        } finally {
            sessionManager.clearSession()
        }
    }
}