package com.gps_g6.bestbiddertcg.feature.profile.domain.model

data class AccountDeletionCheck(
    val hasActiveAuction: Boolean,
    val isWinningAuction: Boolean
) {
    val canDelete: Boolean
        get() = !hasActiveAuction && !isWinningAuction
}