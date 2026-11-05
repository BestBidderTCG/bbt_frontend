package com.gps_g6.bestbiddertcg.feature.auth.domain.model

data class AuthSession(
    val accessToken: String,
    val refreshToken: String
)
