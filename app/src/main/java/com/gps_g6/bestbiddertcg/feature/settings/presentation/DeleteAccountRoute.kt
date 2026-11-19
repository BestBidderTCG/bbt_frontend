package com.gps_g6.bestbiddertcg.feature.settings.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.profile.data.ProfileRepositoryImpl
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.ProfileApi
import com.gps_g6.bestbiddertcg.feature.profile.di.ProfileModule
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.CheckAccountDeletionUseCase
import com.gps_g6.bestbiddertcg.feature.profile.domain.usecase.RequestAccountDeletionUseCase

@Composable
fun DeleteAccountRoute(
    onBackClick: () -> Unit,
    onRequestSucceeded: () -> Unit
) {
    val factory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        DeleteAccountViewModel::class.java
                    )
                )

                val api = NetworkModule.retrofit
                    .create(ProfileApi::class.java)

                val repository = ProfileRepositoryImpl(api)

                val checkUseCase = CheckAccountDeletionUseCase(
                    repository = repository
                )

                val requestUseCase = RequestAccountDeletionUseCase(
                    repository = repository,
                    sessionManager = NetworkModule.sessionManager,
                    profileManager = ProfileModule.profileManager
                )

                return DeleteAccountViewModel(
                    checkAccountDeletion = checkUseCase,
                    requestAccountDeletion = requestUseCase
                ) as T
            }
        }
    }

    val deleteAccountViewModel: DeleteAccountViewModel = viewModel(
        factory = factory
    )

    val state by deleteAccountViewModel.uiState.collectAsState()

    BackHandler(enabled = state.isSubmitting) {
        // Prevent leaving during the deletion request.
    }

    LaunchedEffect(state.requestSucceeded) {
        if (state.requestSucceeded) {
            onRequestSucceeded()
        }
    }

    DeleteAccountScreen(
        uiState = state.screen,
        isSubmitting = state.isSubmitting,
        error = state.error,
        onBackClick = {
            if (!state.isSubmitting) {
                onBackClick()
            }
        },
        onDeleteRequestClick = {
            deleteAccountViewModel.requestDeletion()
        },
        onRetryClick = {
            deleteAccountViewModel.checkDeletion()
        }
    )
}
