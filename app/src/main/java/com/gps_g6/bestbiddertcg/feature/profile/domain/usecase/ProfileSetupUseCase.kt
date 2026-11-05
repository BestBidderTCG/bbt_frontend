package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager

class ProfileSetupUseCase(
    private val profileManager: ProfileManager
) {

    suspend operator fun invoke(
        description: String?,
        avatarNum: Int?
    ) {
        profileManager.updateProfile(
            description = description?.trim()?.takeIf {
                it.isNotEmpty()
            },
            avatarNum = avatarNum ?: 1
        )
    }
}
