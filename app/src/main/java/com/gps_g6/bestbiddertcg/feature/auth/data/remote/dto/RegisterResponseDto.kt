package com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    val id: String,
    val username: String
)
