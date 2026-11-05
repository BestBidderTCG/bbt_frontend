package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.BuildConfig
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import com.gps_g6.bestbiddertcg.feature.auth.domain.validation.AuthValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())

    val uiState = _uiState.asStateFlow()


    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = false,
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = false,
                confirmPasswordError = false,
                errorMessage = null
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = false,
                errorMessage = null
            )
        }
    }

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                usernameError = false,
                errorMessage = null
            )
        }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update {
            it.copy(description = value)
        }
    }

    fun onAvatarSelected(avatarNum: Int) {
        require(avatarNum in 1..9) {
            "Avatar number must be between 1 and 9"
        }

        _uiState.update {
            it.copy(avatarNum = avatarNum)
        }
    }

    fun onPreferenceToggle(category: CardCategory) {
        _uiState.update { state ->

            val updatedPreferences =
                if (category in state.selectedPreferences) {
                    state.selectedPreferences - category
                } else {
                    state.selectedPreferences + category
                }

            state.copy(
                selectedPreferences = updatedPreferences
            )
        }
    }

    fun clearRegistration() {
        _uiState.value = RegisterUiState()
    }

    fun validateCredentials(): Boolean {
        val state = _uiState.value

        val invalidEmail =
            !AuthValidator.isValidEmail(state.email.trim())

        val invalidPassword =
            !AuthValidator.isValidPassword(state.password)

        val invalidConfirmation =
            state.confirmPassword.isEmpty() ||
                    state.password != state.confirmPassword

        _uiState.update {
            it.copy(
                emailError = invalidEmail,
                passwordError = invalidPassword,
                confirmPasswordError = invalidConfirmation
            )
        }

        return !invalidEmail &&
                !invalidPassword &&
                !invalidConfirmation
    }

    fun validateProfileData(): Boolean {
        val state = _uiState.value

        val invalidUsername =
            !AuthValidator.isValidUsername(state.username.trim())

        _uiState.update {
            it.copy(
                usernameError = invalidUsername
            )
        }

        return !invalidUsername
    }

    fun registerAndLogin() {
        val current = _uiState.value

        if (current.isLoading || current.isRegistered) {
            return
        }

        // Revalidate all required account credentials.
        if (!validateCredentials() || !validateProfileData()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Revisa los datos del registro."
                )
            }
            return
        }

        val state = _uiState.value

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            var created = state.accountCreated

            try {
                if (!created) {
                    // 1: create the account
                    withContext(Dispatchers.IO) {
                        repository.register(
                            email = state.email.trim(),
                            username = state.username.trim(),
                            password = state.password
                        )
                    }

                    created = true

                    _uiState.update {
                        it.copy(accountCreated = true)
                    }
                }

                // 2: Login with the new account
                val session = withContext(Dispatchers.IO) {
                    repository.login(
                        identifier = state.email.trim(),
                        password = state.password
                    )
                }

                // 3: Persist the authenticated session
                withContext(Dispatchers.IO) {
                    sessionManager.saveSession(
                        accessToken = session.accessToken,
                        refreshToken = session.refreshToken
                    )
                }

                // Remove passwords after successful authentication.
                _uiState.update {
                    it.copy(
                        password = "",
                        confirmPassword = "",
                        isLoading = false,
                        isRegistered = true,
                        errorMessage = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {

                if (BuildConfig.DEBUG) {
                    val errorBody = e.response()
                        ?.errorBody()
                        ?.string()

                    android.util.Log.e(
                        "BestBidderRegister",
                        "Error HTTP ${e.code()}: $errorBody"
                    )
                }

                val message = if (created) {
                    when (e.code()) {
                        400, 401, 403 ->
                            "La cuenta se ha creado, pero no se ha podido iniciar sesión."
                        else ->
                            "La cuenta se ha creado, pero ha fallado el inicio de sesión."
                    }
                } else {
                    when (e.code()) {
                        400, 409 ->
                            "El email o nombre de usuario puede estar en uso, o los datos no son válidos."
                        429 ->
                            "Demasiados intentos. Inténtalo más tarde."
                        in 500..599 ->
                            "El servidor no está disponible en este momento."
                        else ->
                            "No se ha podido crear la cuenta."
                    }
                }

                _uiState.update {
                    it.copy(
                        accountCreated = created,
                        isLoading = false,
                        errorMessage = message
                    )
                }

            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        accountCreated = created,
                        isLoading = false,
                        errorMessage =
                            "No se ha podido conectar. Comprueba la conexión e inténtalo de nuevo."
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        accountCreated = created,
                        isLoading = false,
                        errorMessage = "Ha ocurrido un error inesperado."
                    )
                }
            }
        }
    }

}
