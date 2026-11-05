package com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDto(
    val username: String? = null,
    val description: String? = null,
    val avatarNum: Int? = null
)
