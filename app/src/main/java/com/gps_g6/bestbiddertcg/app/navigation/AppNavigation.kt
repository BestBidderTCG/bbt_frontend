
package com.gps_g6.bestbiddertcg.app.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gps_g6.bestbiddertcg.app.BestBidderApp
import com.gps_g6.bestbiddertcg.core.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.auth.presentation.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.*

/**
 * Coordinates navigation between authentication,
 * profile creation and the main application.
 *
 * Authentication and profile persistence are not
 * implemented yet.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Login state
    var loginIdentifier by rememberSaveable {
        mutableStateOf("")
    }

    var loginPassword by remember {
        mutableStateOf("")
    }

    // Registration state
    var registerEmail by rememberSaveable {
        mutableStateOf("")
    }

    var registerPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    // Profile creation state
    var username by rememberSaveable {
        mutableStateOf("")
    }

    var description by rememberSaveable {
        mutableStateOf("")
    }

    var avatarNum by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    // Preferences state
    var preferenceIds by rememberSaveable {
        mutableStateOf("")
    }

    var selectedPreferences by rememberSaveable(
        stateSaver = listSaver<Set<CardCategory>, String>(
            save = { categories ->
                categories.map { it.name }
            },
            restore = { names ->
                names.mapNotNull { name ->
                    CardCategory.entries.find {
                        it.name == name
                    }
                }.toSet()
            }
        )
    ) {
        mutableStateOf(emptySet<CardCategory>())
    }


    // Demo-only transition to the main application.
    fun enterMainApp() {
        loginPassword = ""
        registerPassword = ""
        confirmPassword = ""

        navController.navigate(AppDestination.MAIN.route) {
            popUpTo(AppDestination.WELCOME.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.WELCOME.route
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
            LoginScreen(
                identifier = loginIdentifier,
                onIdentifierChange = {
                    loginIdentifier = it
                },
                password = loginPassword,
                onPasswordChange = {
                    loginPassword = it
                },
                onLoginClick = {
                    // Demo only: authentication is pending.
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
                email = registerEmail,
                onEmailChange = {
                    registerEmail = it
                },
                password = registerPassword,
                onPasswordChange = {
                    registerPassword = it
                },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = {
                    confirmPassword = it
                },
                onContinueClick = {
                    // Validation and registration pending.
                    navController.navigate(
                        AppDestination.CREATE_PROFILE.route
                    )
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
                }
            )
        }

        // Create profile - Step 1
        composable(AppDestination.CREATE_PROFILE.route) {
            CreateProfileScreen(
                username = username,
                onUsernameChange = {
                    username = it
                },
                description = description,
                onDescriptionChange = {
                    description = it
                },
                avatarNum = avatarNum,
                onAvatarClick = {
                    navController.navigate(
                        AppDestination.AVATAR_SELECTION.route
                    )
                },
                onContinueClick = {
                    navController.navigate(
                        AppDestination.PREFERENCES.route
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Avatar selection
        composable(AppDestination.AVATAR_SELECTION.route) {
            AvatarSelectionScreen(
                selectedAvatar = avatarNum,
                onAvatarSelected = {
                    avatarNum = it
                },
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
            PreferencesScreen(
                selectedPreferences = selectedPreferences,
                onPreferenceToggle = { category ->
                    selectedPreferences =
                        if (category in selectedPreferences) {
                            selectedPreferences - category
                        } else {
                            selectedPreferences + category
                        }
                },
                onFinishClick = {
                    // Demo only: profile is not persisted.
                    enterMainApp()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }


        // Main application
        composable(AppDestination.MAIN.route) {
            BestBidderApp()
        }
    }
}
