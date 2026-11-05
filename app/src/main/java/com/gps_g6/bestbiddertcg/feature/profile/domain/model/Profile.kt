package com.gps_g6.bestbiddertcg.feature.profile.domain.model

data class Profile(
    val id: String,
    val username: String,
    val email: String,
    val description: String?,
    val avatarNum: Int,
    val createdAt: String
)
