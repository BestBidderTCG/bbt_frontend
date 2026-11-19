package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestAccountDeletionUseCase(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val profileManager: ProfileManager
) {
    suspend operator fun invoke() {
        val result = repository.requestAccountDeletion()

        check(result.status == "PENDING_DELETION") {
            "Unexpected account deletion status: ${result.status}"
        }

        // The backend accepted the request and revoked refresh tokens
        // Remove local credentials and cached profile
        withContext(Dispatchers.IO) {
            sessionManager.clearSession()
            profileManager.clearProfile()
        }
    }
}
