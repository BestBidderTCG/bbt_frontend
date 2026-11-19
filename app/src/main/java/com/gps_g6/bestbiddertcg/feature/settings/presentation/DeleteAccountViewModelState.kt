package com.gps_g6.bestbiddertcg.feature.settings.presentation

data class DeleteAccountViewModelState(
    val screen: DeleteAccountUiState = DeleteAccountUiState.Loading,
    val isSubmitting: Boolean = false,
    val requestSucceeded: Boolean = false,
    val error: DeleteAccountError? = null
)
