package com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDeletionStatusDto(
    val status: String,
    val deletionRequestedAt: String? = null,
    val scheduledPurgeAt: String? = null
)
