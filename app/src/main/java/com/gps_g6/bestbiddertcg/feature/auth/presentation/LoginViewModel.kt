
package com.gps_g6.bestbiddertcg.feature.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.BuildConfig
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onIdentifierChange(identifier: String) {
        _uiState.value = _uiState.value.copy(
            identifier = identifier,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }


    fun login() {
        if (BuildConfig.DEBUG) {
            Log.d("BestBidderAuth", "1. LoginViewModel.login() ejecutado")
        }

        val state = _uiState.value

        if (state.isLoading || state.isAuthenticated) {
            if (BuildConfig.DEBUG) {
                Log.d("BestBidderAuth", "Login ignorado: operación en curso o sesión iniciada")
            }
            return
        }

        if (state.identifier.isBlank() || state.password.isEmpty()) {
            if (BuildConfig.DEBUG) {
                Log.d("BestBidderAuth", "Validación fallida: campos vacíos")
            }

            _uiState.value = state.copy(
                errorMessage = "Introduce tu usuario o correo y contraseña."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isLoading = true,
                errorMessage = null
            )

            if (BuildConfig.DEBUG) {
                Log.d("BestBidderAuth", "2. Iniciando petición al repositorio")
            }

            try {
                val session = repository.login(
                    identifier = state.identifier,
                    password = state.password
                )

                if (BuildConfig.DEBUG) {
                    Log.d("BestBidderAuth", "3. Respuesta de login recibida")
                }

                sessionManager.saveSession(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken
                )

                if (BuildConfig.DEBUG) {
                    Log.d("BestBidderAuth", "4. Sesión guardada correctamente")
                }

                _uiState.value = _uiState.value.copy(
                    password = "",
                    isLoading = false,
                    isAuthenticated = true,
                    errorMessage = null
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {
                if (BuildConfig.DEBUG) {
                    Log.e("BestBidderAuth", "Error HTTP: ${e.code()}")
                }

                val message = when (e.code()) {
                    400, 401 -> "Usuario o contraseña incorrectos."
                    429 -> "Demasiados intentos. Inténtalo más tarde."
                    in 500..599 -> "El servidor no está disponible."
                    else -> "No se ha podido iniciar sesión."
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )

            } catch (e: IOException) {
                if (BuildConfig.DEBUG) {
                    Log.e("BestBidderAuth", "Error de conexión", e)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se ha podido conectar con el servidor."
                )

            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e("BestBidderAuth", "Error inesperado", e)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Ha ocurrido un error inesperado."
                )
            }
        }
    }

}
