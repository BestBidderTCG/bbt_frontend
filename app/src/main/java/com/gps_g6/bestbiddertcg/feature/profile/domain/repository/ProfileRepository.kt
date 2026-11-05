package com.gps_g6.bestbiddertcg.feature.profile.domain.repository

import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile

interface ProfileRepository {

    suspend fun getCurrentProfile(): Profile

    suspend fun updateProfile(
        username: String?,
        description: String?,
        avatarNum: Int?
    ): Profile
}
