package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RestoreSessionUseCase(
    private val sessionManager: SessionManager,
    private val refreshSession: RefreshSessionUseCase
) {

    suspend operator fun invoke(): Boolean {
        // restore de encrypted tokens
        val hasStoredSession = withContext(Dispatchers.IO) {
            sessionManager.restoreSession()
        }

        if (!hasStoredSession) {
            return false
        }

        return try {
            // backend valida el refresh token.
            refreshSession()
        } catch (e: HttpException) {
            when (e.code()) {
                400, 401, 403 -> {
                    // invalid or rejected refresh token
                    withContext(Dispatchers.IO) {
                        sessionManager.clearSession()
                    }
                    false
                }

                else -> throw e
            }
        }
    }
}
