package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository

class RefreshSessionUseCase(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(): Boolean {
        val refreshToken = sessionManager.getRefreshToken()
            ?: return false

        val session = repository.refresh(refreshToken)

        sessionManager.saveSession(
            accessToken = session.accessToken,
            refreshToken = session.refreshToken
        )

        return true
    }
}
