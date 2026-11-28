package com.gps_g6.bestbiddertcg.feature.notification.data.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.notification.data.remote.DeviceApi
import com.gps_g6.bestbiddertcg.feature.notification.data.remote.dto.RegisterDeviceRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FcmTokenManager(
    private val deviceApi: DeviceApi =
        NetworkModule.retrofit.create(DeviceApi::class.java)
) {

    suspend fun registerCurrentDevice() {
        val token = getCurrentToken()
        registerToken(token)
    }

    suspend fun registerToken(token: String) {
        require(token.isNotBlank())

        val response = deviceApi.registerDevice(
            RegisterDeviceRequest(
                token = token,
                platform = "ANDROID"
            )
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun unregisterCurrentDevice() {
        val token = getCurrentToken()

        val response = deviceApi.unregisterDevice(token)

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    private suspend fun getCurrentToken(): String =
        suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    if (continuation.isActive) {
                        continuation.resume(token)
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }
        }
}
