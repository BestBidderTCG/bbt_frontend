package com.gps_g6.bestbiddertcg.feature.auth.presentation

import com.gps_g6.bestbiddertcg.core.model.CardCategory

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    val description: String = "",
    val avatarNum: Int? = null,
    val selectedPreferences: Set<CardCategory> = emptySet(),

    // Field validation
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,
    val usernameError: Boolean = false,

    // Registration request
    val isLoading: Boolean = false,
    val accountCreated: Boolean = false,
    val loginCompleted: Boolean = false,
    val isRegistered: Boolean = false,
    val errorMessage: String? = null

)
