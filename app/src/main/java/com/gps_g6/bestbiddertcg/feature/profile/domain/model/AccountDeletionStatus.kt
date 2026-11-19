package com.gps_g6.bestbiddertcg.feature.profile.domain.model

data class AccountDeletionStatus(
    val status: String,
    val deletionRequestedAt: String?,
    val scheduledPurgeAt: String?
)
