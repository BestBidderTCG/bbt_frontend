package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(
    private val profileManager: ProfileManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(isLoading = true)
    )

    val uiState = _uiState.asStateFlow()

    private var loadInProgress = false

    init {
        observeProfile()
        loadProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            profileManager.profile.collect { profile ->

                if (profile != null) {
                    _uiState.update {
                        it.withProfile(profile)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            username = "",
                            email = "",
                            description = null,
                            avatarNum = null
                        )
                    }
                }
            }
        }
    }

    fun loadProfile(
        forceRefresh: Boolean = false
    ) {
        if (loadInProgress) return

        loadInProgress = true

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val profile = profileManager.loadProfile(
                    forceRefresh = forceRefresh
                )

                _uiState.update {
                    it.withProfile(profile).copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {
                val message = when (e.code()) {
                    401, 403 ->
                        "No se ha podido acceder al perfil. Inicia sesión de nuevo."

                    404 ->
                        "No se ha encontrado el perfil."

                    in 500..599 ->
                        "El servidor no está disponible en este momento."

                    else ->
                        "No se ha podido cargar el perfil."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }

            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se ha podido conectar con el servidor."
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ha ocurrido un error al cargar el perfil."
                    )
                }

            } finally {
                loadInProgress = false
            }
        }
    }

    private fun ProfileUiState.withProfile(
        profile: Profile
    ): ProfileUiState {
        return copy(
            username = profile.username,
            email = profile.email,
            description = profile.description,
            avatarNum = profile.avatarNum
        )
    }
}
