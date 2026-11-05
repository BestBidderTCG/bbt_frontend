package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException

class LogoutUseCase(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke() {
        val refreshToken = sessionManager.getRefreshToken()

        try {
            if (!refreshToken.isNullOrBlank()) {
                repository.logout(refreshToken)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Remote revocation may fail, but local logout proceeds.
        } finally {
            sessionManager.clearSession()
        }
    }
}
