package com.gps_g6.bestbiddertcg.feature.settings.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.auth.data.AuthRepositoryImpl
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.LogoutUseCase
import com.gps_g6.bestbiddertcg.feature.auth.presentation.LogoutViewModel
import com.gps_g6.bestbiddertcg.feature.profile.di.ProfileModule

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current

    val factory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        LogoutViewModel::class.java
                    )
                )

                val api = NetworkModule.retrofit
                    .create(AuthApi::class.java)

                val repository = AuthRepositoryImpl(api)

                val logoutUseCase = LogoutUseCase(
                    repository = repository,
                    sessionManager = NetworkModule.sessionManager
                )

                return LogoutViewModel(logoutUseCase) as T
            }
        }
    }

    val logoutViewModel: LogoutViewModel = viewModel(
        factory = factory
    )

    val logoutState by logoutViewModel.uiState.collectAsState()

    BackHandler(enabled = logoutState.isLoading) {
        // Prevent leaving Settings while logout is running
    }

    var notificationsEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    // Logout finished: discard cached profile and leave
    // the authenticated navigation graph.
    LaunchedEffect(logoutState.isLoggedOut) {
        if (logoutState.isLoggedOut) {
            ProfileModule.profileManager.clearProfile()
            onLogoutSuccess()
        }
    }

    // Display any local logout error.
    LaunchedEffect(logoutState.errorMessage) {
        logoutState.errorMessage?.let { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun showNotAvailable() {
        Toast.makeText(
            context,
            "Funcionalidad todavía no disponible",
            Toast.LENGTH_SHORT
        ).show()
    }

    SettingsScreen(
        notificationsEnabled = notificationsEnabled,
        onNotificationsChange = { enabled ->
            notificationsEnabled = enabled
        },
        onChangePasswordClick = {
            showNotAvailable()
        },
        onPrivacyClick = {
            showNotAvailable()
        },
        onTermsClick = {
            showNotAvailable()
        },
        onLogoutClick = {
            logoutViewModel.logout()
        },
        onDeleteAccountClick = {
            if (!logoutState.isLoading) {
                onDeleteAccountClick()
            }
        },
        onBackClick = {
            if (!logoutState.isLoading) {
                onBackClick()
            }
        },
        isLoggingOut = logoutState.isLoading,
    )
}
