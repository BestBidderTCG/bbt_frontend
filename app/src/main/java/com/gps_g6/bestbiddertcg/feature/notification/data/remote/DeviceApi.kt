package com.gps_g6.bestbiddertcg.feature.notification.data.remote

import com.gps_g6.bestbiddertcg.feature.notification.data.remote.dto.RegisterDeviceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface DeviceApi {

    @POST("api/devices")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): Response<Unit>

    @DELETE("api/devices")
    suspend fun unregisterDevice(
        @Query("token") token: String
    ): Response<Unit>

}
