package com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    val username: String,
    val email: String,
    val description: String? = null,
    val avatarNum: Int,
    val createdAt: String
)
