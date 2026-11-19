package com.gps_g6.bestbiddertcg.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDeletionCheckDto(
    val hasActiveAuction: Boolean,
    val isWinningAuction: Boolean
)
