
package com.gps_g6.bestbiddertcg.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gps_g6.bestbiddertcg.app.BestBidderApp
import com.gps_g6.bestbiddertcg.core.model.CardCategory
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.auth.data.AuthRepositoryImpl
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.RefreshSessionUseCase
import com.gps_g6.bestbiddertcg.feature.auth.domain.usecase.RestoreSessionUseCase
import com.gps_g6.bestbiddertcg.feature.auth.presentation.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.*

/**
 * Coordinates navigation between authentication,
 * profile creation and the main application.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val registerFactory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        RegisterViewModel::class.java
                    )
                )

                val api = NetworkModule.retrofit
                    .create(AuthApi::class.java)

                val repository = AuthRepositoryImpl(api)

                return RegisterViewModel(
                    repository = repository,
                    sessionManager = NetworkModule.sessionManager
                ) as T
            }
        }
    }

    val registerViewModel: RegisterViewModel = viewModel(
        factory = registerFactory
    )

    val registerState by registerViewModel.uiState.collectAsState()

    val startupFactory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        StartupViewModel::class.java
                    )
                )

                val api = NetworkModule.retrofit
                    .create(AuthApi::class.java)

                val repository = AuthRepositoryImpl(api)

                val refreshSession = RefreshSessionUseCase(
                    repository = repository,
                    sessionManager = NetworkModule.sessionManager
                )

                val restoreSession = RestoreSessionUseCase(
                    sessionManager = NetworkModule.sessionManager,
                    refreshSession = refreshSession
                )

                return StartupViewModel(
                    restoreSession = restoreSession,
                    sessionManager = NetworkModule.sessionManager
                ) as T
            }
        }
    }

    val startupViewModel: StartupViewModel = viewModel(
        factory = startupFactory
    )

    val startupState by startupViewModel.uiState.collectAsState()

    fun enterMainApp() {
        if (!NetworkModule.sessionManager.isAuthenticated()) {
            return
        }

        navController.navigate(AppDestination.MAIN.route) {
            popUpTo(AppDestination.WELCOME.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }


    when (val state = startupState) {

        StartupUiState.Checking -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        is StartupUiState.ConnectionError -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = state.message)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = startupViewModel::checkSession
                ) {
                    Text("Reintentar")
                }

                TextButton(
                    onClick = startupViewModel::discardSessionAndContinue
                ) {
                    Text("Cerrar sesión")
                }
            }
            return
        }

        StartupUiState.Authenticated,
        StartupUiState.Unauthenticated -> Unit
    }

    val initialDestination = when (startupState) {
        StartupUiState.Authenticated -> AppDestination.MAIN.route
        else -> AppDestination.WELCOME.route
    }

    NavHost(
        navController = navController,
        startDestination = initialDestination
    ) {

    // Welcome
        composable(AppDestination.WELCOME.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(
                        AppDestination.LOGIN.route
                    )
                },
                onRegisterClick = {
                    navController.navigate(
                        AppDestination.REGISTER.route
                    )
                }
            )
        }

        // Login
        composable(AppDestination.LOGIN.route) {
            LoginRoute(
                onLoginSuccess = {
                    enterMainApp()
                },
                onRegisterClick = {
                    navController.navigate(
                        AppDestination.REGISTER.route
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }


        // Register
        composable(AppDestination.REGISTER.route) {
            RegisterScreen(
                email = registerState.email,
                onEmailChange = registerViewModel::onEmailChange,

                password = registerState.password,
                onPasswordChange = registerViewModel::onPasswordChange,

                confirmPassword = registerState.confirmPassword,
                onConfirmPasswordChange =
                    registerViewModel::onConfirmPasswordChange,


                onContinueClick = {
                    if (registerViewModel.validateCredentials()) {
                        navController.navigate(
                            AppDestination.CREATE_PROFILE.route
                        )
                    }
                },

                onLoginClick = {
                    navController.navigate(
                        AppDestination.LOGIN.route
                    ) {
                        popUpTo(AppDestination.WELCOME.route)
                        launchSingleTop = true
                    }
                },

                onBackClick = {
                    navController.popBackStack()
                },

                emailError = registerState.emailError,
                passwordError = registerState.passwordError,
                confirmPasswordError = registerState.confirmPasswordError

            )
        }

        // Create profile - Step 1
        composable(AppDestination.CREATE_PROFILE.route) {
            CreateProfileScreen(
                username = registerState.username,
                onUsernameChange = registerViewModel::onUsernameChange,

                description = registerState.description,
                onDescriptionChange =
                    registerViewModel::onDescriptionChange,

                avatarNum = registerState.avatarNum,

                onAvatarClick = {
                    navController.navigate(
                        AppDestination.AVATAR_SELECTION.route
                    )
                },

                onContinueClick = {
                    if (registerViewModel.validateProfileData()) {
                        navController.navigate(
                            AppDestination.PREFERENCES.route
                        )
                    }
                },

                onBackClick = {
                    navController.popBackStack()
                },

                usernameError = registerState.usernameError

            )
        }

        // Avatar selection
        composable(AppDestination.AVATAR_SELECTION.route) {
            AvatarSelectionScreen(
                selectedAvatar = registerState.avatarNum,

                onAvatarSelected =
                    registerViewModel::onAvatarSelected,

                onConfirmClick = {
                    navController.popBackStack()
                },

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Preferences - Step 2
        composable(AppDestination.PREFERENCES.route) {

            LaunchedEffect(registerState.isRegistered) {
                if (registerState.isRegistered) {
                    enterMainApp()
                    registerViewModel.clearRegistration()
                }
            }

            PreferencesScreen(
                selectedPreferences = registerState.selectedPreferences,

                onPreferenceToggle =
                    registerViewModel::onPreferenceToggle,

                onFinishClick = {
                    registerViewModel.registerAndLogin()
                },

                onBackClick = {
                    if (
                        !registerState.isLoading &&
                        !registerState.accountCreated
                    ) {
                        navController.popBackStack()
                    }
                },

                isLoading = registerState.isLoading,
                errorMessage = registerState.errorMessage
            )
        }

        // Main application
        composable(AppDestination.MAIN.route) {
            BestBidderApp()
        }
    }
}
