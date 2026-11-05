package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.RestoreSessionUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Recupera y valida una sesión previamente guardada.
 */
class StartupViewModel(
    private val restoreSession: RestoreSessionUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartupUiState>(
        StartupUiState.Checking
    )

    val uiState = _uiState.asStateFlow()

    private var checkingInProgress = false

    init {
        checkSession()
    }

    fun checkSession() {
        if (checkingInProgress) return

        checkingInProgress = true

        viewModelScope.launch {
            _uiState.value = StartupUiState.Checking

            try {
                val authenticated = restoreSession()

                _uiState.value = if (authenticated) {
                    StartupUiState.Authenticated
                } else {
                    StartupUiState.Unauthenticated
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _uiState.value = StartupUiState.ConnectionError(
                    "No se ha podido conectar con el servidor."
                )

            } catch (e: HttpException) {
                _uiState.value = StartupUiState.ConnectionError(
                    "No se ha podido comprobar la sesión."
                )

            } catch (e: Exception) {
                _uiState.value = StartupUiState.ConnectionError(
                    "Ha ocurrido un error al recuperar la sesión."
                )

            } finally {
                checkingInProgress = false
            }
        }
    }

    fun discardSessionAndContinue() {
        if (checkingInProgress) return

        viewModelScope.launch {
            _uiState.value = StartupUiState.Checking

            try {
                withContext(Dispatchers.IO) {
                    sessionManager.clearSession()
                }

                _uiState.value = StartupUiState.Unauthenticated

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = StartupUiState.ConnectionError(
                    "No se ha podido eliminar la sesión local."
                )
            }
        }
    }
}
