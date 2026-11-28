package com.gps_g6.bestbiddertcg.feature.notification.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val token: String,
    val platform: String
)
