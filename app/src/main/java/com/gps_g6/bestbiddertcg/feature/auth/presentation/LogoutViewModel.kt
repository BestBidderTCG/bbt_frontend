package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.LogoutUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogoutUiState())
    val uiState = _uiState.asStateFlow()

    fun logout() {
        if (_uiState.value.isLoading ||
            _uiState.value.isLoggedOut
        ) return

        viewModelScope.launch {
            _uiState.value = LogoutUiState(
                isLoading = true
            )

            try {
                logoutUseCase()

                _uiState.value = LogoutUiState(
                    isLoggedOut = true
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = LogoutUiState(
                    errorMessage = "No se ha podido cerrar la sesión."
                )
            }
        }
    }
}
