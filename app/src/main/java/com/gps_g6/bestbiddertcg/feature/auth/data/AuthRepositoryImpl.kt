package com.gps_g6.bestbiddertcg.feature.auth.data

import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.LoginRequestDto
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.LogoutRequestDto
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.RefreshRequestDto
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.RegisterRequestDto
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.AuthSession
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.RegisteredUser
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): RegisteredUser {

        val response = api.register(
            RegisterRequestDto(
                email = email,
                username = username,
                password = password
            )
        )

        return RegisteredUser(
            id = response.id,
            username = response.username
        )
    }

    override suspend fun login(
        identifier: String,
        password: String
    ): AuthSession {

        val response = api.login(
            LoginRequestDto(
                identifier = identifier,
                password = password
            )
        )

        return AuthSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    override suspend fun refresh(
        refreshToken: String
    ): AuthSession {

        val response = api.refresh(
            RefreshRequestDto(
                refreshToken = refreshToken
            )
        )

        return AuthSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    override suspend fun logout(refreshToken: String) {
        api.logout(
            LogoutRequestDto(
                refreshToken = refreshToken
            )
        )
    }

}
