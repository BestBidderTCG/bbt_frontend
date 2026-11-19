package com.gps_g6.bestbiddertcg.feature.profile.data.remote

import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.AccountDeletionCheckDto
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.AccountDeletionStatusDto
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.ProfileDto
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {

    @GET("api/users/me")
    suspend fun getCurrentProfile(): ProfileDto

    @PATCH("api/users/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): ProfileDto

    @GET("api/users/me/deletion-check")
    suspend fun checkAccountDeletion(): AccountDeletionCheckDto

    @DELETE("api/users/me")
    suspend fun requestAccountDeletion(): AccountDeletionStatusDto

}
