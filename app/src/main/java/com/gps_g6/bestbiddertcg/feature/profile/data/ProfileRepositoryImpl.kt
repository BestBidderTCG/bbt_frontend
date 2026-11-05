package com.gps_g6.bestbiddertcg.feature.profile.data

import com.gps_g6.bestbiddertcg.feature.profile.data.remote.ProfileApi
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.ProfileDto
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.UpdateProfileRequestDto
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getCurrentProfile(): Profile {
        return api.getCurrentProfile().toDomain()
    }

    override suspend fun updateProfile(
        username: String?,
        description: String?,
        avatarNum: Int?
    ): Profile {
        return api.updateProfile(
            UpdateProfileRequestDto(
                username = username,
                description = description,
                avatarNum = avatarNum
            )
        ).toDomain()
    }

    private fun ProfileDto.toDomain(): Profile {
        return Profile(
            id = id,
            username = username,
            email = email,
            description = description,
            avatarNum = avatarNum?.takeIf { it in 1..9 } ?: 1,
            createdAt = createdAt
        )
    }

}
