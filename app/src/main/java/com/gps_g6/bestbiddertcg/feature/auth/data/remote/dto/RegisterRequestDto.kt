package com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val username: String,
    val password: String
)
