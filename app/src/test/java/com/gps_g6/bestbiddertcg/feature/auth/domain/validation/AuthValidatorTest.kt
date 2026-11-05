package com.gps_g6.bestbiddertcg.feature.auth.domain.validation

import org.junit.Assert.*
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `valid emails are accepted`() {
        assertTrue(AuthValidator.isValidEmail("bestbidder@gmail.com"))
        assertTrue(AuthValidator.isValidEmail("user.name@example.es"))
    }

    @Test
    fun `invalid emails are rejected`() {
        assertFalse(AuthValidator.isValidEmail("bestbidder gmail.com"))
        assertFalse(AuthValidator.isValidEmail("bestbidder@gmail"))
        assertFalse(AuthValidator.isValidEmail("bestbidder@.com"))
        assertFalse(AuthValidator.isValidEmail("bestbidder@gmail."))
    }

    @Test
    fun `valid usernames are accepted`() {
        assertTrue(AuthValidator.isValidUsername("bestbidder"))
        assertTrue(AuthValidator.isValidUsername("b77"))
        assertTrue(AuthValidator.isValidUsername("bestbidder_2026"))
        assertTrue(AuthValidator.isValidUsername("bestbidder.tcg"))
        assertTrue(AuthValidator.isValidUsername("b".repeat(50)))
    }

    @Test
    fun `invalid usernames are rejected`() {
        assertFalse(AuthValidator.isValidUsername("bb"))
        assertFalse(AuthValidator.isValidUsername("777"))
        assertFalse(AuthValidator.isValidUsername("BestBidder"))
        assertFalse(AuthValidator.isValidUsername("bestbidder tcg"))
        assertFalse(AuthValidator.isValidUsername("b".repeat(51)))
    }

    @Test
    fun `password length is validated`() {
        assertFalse(AuthValidator.isValidPassword("12345"))
        assertTrue(AuthValidator.isValidPassword("123456"))
        assertTrue(AuthValidator.isValidPassword("b".repeat(50)))
        assertFalse(AuthValidator.isValidPassword("b".repeat(51)))
    }

    @Test
    fun `password confirmation must match`() {
        assertTrue(
            AuthValidator.doPasswordsMatch("password123", "password123")
        )

        assertFalse(
            AuthValidator.doPasswordsMatch("password123", "different")
        )
    }

    @Test
    fun `login accepts email or username`() {
        assertTrue(
            AuthValidator.isValidLoginIdentifier("bestbidder@gmail.com")
        )
        assertTrue(
            AuthValidator.isValidLoginIdentifier("bestbidder_2026")
        )
        assertFalse(
            AuthValidator.isValidLoginIdentifier("777")
        )
    }
}
