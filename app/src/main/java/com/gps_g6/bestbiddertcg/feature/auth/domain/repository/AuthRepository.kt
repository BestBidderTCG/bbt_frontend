package com.gps_g6.bestbiddertcg.feature.auth.domain.repository

import com.gps_g6.bestbiddertcg.feature.auth.domain.model.AuthSession
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.RegisteredUser

interface AuthRepository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): RegisteredUser

    suspend fun login(
        identifier: String,
        password: String
    ): AuthSession

    suspend fun refresh(
        refreshToken: String
    ): AuthSession

    suspend fun logout(
        refreshToken: String
    )

}
