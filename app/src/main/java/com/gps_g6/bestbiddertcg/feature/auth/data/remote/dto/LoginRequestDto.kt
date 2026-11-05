package com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val identifier: String,
    val password: String
)
