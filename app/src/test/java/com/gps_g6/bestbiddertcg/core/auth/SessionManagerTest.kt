package com.gps_g6.bestbiddertcg.core.auth

import org.junit.Assert.*
import org.junit.Test

class SessionManagerTest {

    private class FakeTokenStorage : TokenStorage {

        private var tokens: SessionTokens? = null

        override fun saveTokens(tokens: SessionTokens) {
            this.tokens = tokens
        }

        override fun getTokens(): SessionTokens? {
            return tokens
        }

        override fun clearTokens() {
            tokens = null
        }
    }

    @Test
    fun `new session is not authenticated`() {
        val manager = SessionManager(FakeTokenStorage())

        assertFalse(manager.isAuthenticated())
        assertNull(manager.getAccessToken())
        assertNull(manager.getRefreshToken())
    }

    @Test
    fun `saving tokens activates session`() {
        val manager = SessionManager(FakeTokenStorage())

        manager.saveSession(
            accessToken = "test-access-token",
            refreshToken = "test-refresh-token"
        )

        assertTrue(manager.isAuthenticated())
        assertEquals(
            "test-access-token",
            manager.getAccessToken()
        )
        assertEquals(
            "test-refresh-token",
            manager.getRefreshToken()
        )
    }

    @Test
    fun `clearing session removes tokens`() {
        val manager = SessionManager(FakeTokenStorage())

        manager.saveSession(
            accessToken = "test-access-token",
            refreshToken = "test-refresh-token"
        )

        manager.clearSession()

        assertFalse(manager.isAuthenticated())
        assertNull(manager.getAccessToken())
        assertNull(manager.getRefreshToken())
    }

    @Test
    fun `empty tokens are rejected`() {
        val manager = SessionManager(FakeTokenStorage())

        assertThrows(IllegalArgumentException::class.java) {
            manager.saveSession(
                accessToken = "",
                refreshToken = "test-refresh-token"
            )
        }

        assertFalse(manager.isAuthenticated())
    }

    @Test
    fun `saving a new session replaces previous tokens`() {
        val manager = SessionManager(FakeTokenStorage())

        manager.saveSession("access-1", "refresh-1")
        manager.saveSession("access-2", "refresh-2")

        assertEquals("access-2", manager.getAccessToken())
        assertEquals("refresh-2", manager.getRefreshToken())
    }

    @Test
    fun `session can be restored from token storage`() {
        val storage = FakeTokenStorage()

        val firstManager = SessionManager(storage)

        firstManager.saveSession(
            accessToken = "access-test",
            refreshToken = "refresh-test"
        )

        // Simulates creating a new manager after restarting.
        val secondManager = SessionManager(storage)

        assertFalse(secondManager.isAuthenticated())

        val restored = secondManager.restoreSession()

        assertTrue(restored)
        assertTrue(secondManager.isAuthenticated())
        assertEquals(
            "access-test",
            secondManager.getAccessToken()
        )
    }

    @Test
    fun `clearing session removes persisted tokens`() {
        val storage = FakeTokenStorage()
        val manager = SessionManager(storage)

        manager.saveSession("access-test", "refresh-test")

        manager.clearSession()

        val newManager = SessionManager(storage)

        assertFalse(newManager.restoreSession())
        assertFalse(newManager.isAuthenticated())
    }

}
