package com.gps_g6.bestbiddertcg.feature.auth.presentation

sealed interface StartupUiState {

    data object Checking : StartupUiState

    data object Authenticated : StartupUiState

    data object Unauthenticated : StartupUiState

    data class ConnectionError(
        val message: String
    ) : StartupUiState
}
