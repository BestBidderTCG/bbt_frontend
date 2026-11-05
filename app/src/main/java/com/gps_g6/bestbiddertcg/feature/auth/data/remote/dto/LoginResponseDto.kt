package com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String
)
