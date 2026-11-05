package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.auth.data.AuthRepositoryImpl
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val factory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        LoginViewModel::class.java
                    )
                )

                val api = NetworkModule.retrofit
                    .create(AuthApi::class.java)

                val repository = AuthRepositoryImpl(api)

                return LoginViewModel(
                    repository = repository,
                    sessionManager = NetworkModule.sessionManager
                ) as T
            }
        }
    }

    val viewModel: LoginViewModel = viewModel(
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        identifier = uiState.identifier,
        onIdentifierChange = viewModel::onIdentifierChange,
        password = uiState.password,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onRegisterClick = onRegisterClick,
        onBackClick = onBackClick,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage
    )
}
