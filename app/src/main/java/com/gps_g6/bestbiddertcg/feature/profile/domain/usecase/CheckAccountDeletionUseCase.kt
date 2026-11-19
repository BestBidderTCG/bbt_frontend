package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.feature.profile.domain.model.AccountDeletionCheck
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository

class CheckAccountDeletionUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): AccountDeletionCheck {
        return repository.checkAccountDeletion()
    }
}
