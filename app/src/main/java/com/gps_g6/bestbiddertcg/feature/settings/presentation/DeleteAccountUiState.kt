package com.gps_g6.bestbiddertcg.feature.settings.presentation

sealed interface DeleteAccountUiState {

    data object Loading : DeleteAccountUiState

    data object Allowed : DeleteAccountUiState

    data class Blocked(
        val hasActiveAuction: Boolean,
        val isWinningAuction: Boolean
    ) : DeleteAccountUiState

    data object Error : DeleteAccountUiState
}
