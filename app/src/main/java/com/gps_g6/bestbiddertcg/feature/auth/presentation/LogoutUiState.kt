package com.gps_g6.bestbiddertcg.feature.auth.presentation

data class LogoutUiState(
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val errorMessage: String? = null
)
