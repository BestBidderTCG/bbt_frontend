package com.gps_g6.bestbiddertcg.feature.auth.data.remote

import com.gps_g6.bestbiddertcg.feature.auth.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @POST("api/auth/logout")
    suspend fun logout(
        @Body request: LogoutRequestDto
    )

    @POST("api/auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequestDto
    ): LoginResponseDto

}
