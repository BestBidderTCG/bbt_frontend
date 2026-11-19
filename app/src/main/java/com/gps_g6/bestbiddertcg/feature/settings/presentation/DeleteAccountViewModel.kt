package com.gps_g6.bestbiddertcg.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.CheckAccountDeletionUseCase
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.RequestAccountDeletionUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DeleteAccountViewModel(
    private val checkAccountDeletion: CheckAccountDeletionUseCase,
    private val requestAccountDeletion: RequestAccountDeletionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DeleteAccountViewModelState()
    )

    val uiState = _uiState.asStateFlow()

    private var isChecking = false

    init {
        checkDeletion()
    }

    fun checkDeletion() {
        if (isChecking ||
            _uiState.value.isSubmitting ||
            _uiState.value.requestSucceeded
        ) return

        isChecking = true

        viewModelScope.launch {
            _uiState.value = DeleteAccountViewModelState()

            try {
                val result = checkAccountDeletion()

                _uiState.value = DeleteAccountViewModelState(
                    screen = if (result.canDelete) {
                        DeleteAccountUiState.Allowed
                    } else {
                        DeleteAccountUiState.Blocked(
                            hasActiveAuction = result.hasActiveAuction,
                            isWinningAuction = result.isWinningAuction
                        )
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DeleteAccountViewModelState(
                    screen = DeleteAccountUiState.Error,
                    error = DeleteAccountError.CHECK_FAILED
                )
            } finally {
                isChecking = false
            }
        }
    }

    fun requestDeletion() {
        val currentState = _uiState.value

        if (
            currentState.screen != DeleteAccountUiState.Allowed ||
            currentState.isSubmitting ||
            currentState.requestSucceeded
        ) return

        // Prevent repeated submissions immediately.
        _uiState.value = currentState.copy(
            isSubmitting = true,
            error = null
        )

        viewModelScope.launch {
            try {
                requestAccountDeletion()

                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    requestSucceeded = true,
                    error = null
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {
                if (e.code() == 409) {
                    handleDeletionConflict()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = DeleteAccountError.REQUEST_FAILED
                    )
                }

            } catch (e: IOException) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = DeleteAccountError.REQUEST_UNCERTAIN
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = DeleteAccountError.REQUEST_UNCERTAIN
                )
            }
        }
    }

    private suspend fun handleDeletionConflict() {
        try {
            val result = checkAccountDeletion()

            _uiState.value = DeleteAccountViewModelState(
                screen = if (result.canDelete) {
                    DeleteAccountUiState.Error
                } else {
                    DeleteAccountUiState.Blocked(
                        hasActiveAuction = result.hasActiveAuction,
                        isWinningAuction = result.isWinningAuction
                    )
                },
                error = if (result.canDelete) {
                    DeleteAccountError.CONFLICT
                } else {
                    null
                }
            )

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {
            _uiState.value = DeleteAccountViewModelState(
                screen = DeleteAccountUiState.Error,
                error = DeleteAccountError.CONFLICT_CHECK_FAILED
            )
        }
    }
}
