package com.gps_g6.bestbiddertcg.feature.auth.domain.validation

object AuthValidator {

    private val emailRegex = Regex(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$"
    )

    private val usernameRegex = Regex(
        "^(?=.{3,50}$)(?=.*[a-z])[a-z0-9_.]+$"
    )

    fun isValidEmail(email: String): Boolean {
        if (email.length > 254) return false
        return emailRegex.matches(email)
    }

    fun isValidUsername(username: String): Boolean {
        return usernameRegex.matches(username)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length in 6..50
    }

    fun doPasswordsMatch(
        password: String,
        confirmPassword: String
    ): Boolean {
        return password == confirmPassword
    }

    fun isValidLoginIdentifier(identifier: String): Boolean {
        return isValidEmail(identifier) ||
                isValidUsername(identifier)
    }
}
