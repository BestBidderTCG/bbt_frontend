package com.gps_g6.bestbiddertcg.feature.auth.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.auth.SessionTokens
import com.gps_g6.bestbiddertcg.core.auth.TokenStorage
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.AuthSession
import com.gps_g6.bestbiddertcg.feature.auth.domain.model.RegisteredUser
import com.gps_g6.bestbiddertcg.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class RefreshSessionUseCaseTest {

    private class FakeTokenStorage : TokenStorage {

        private var tokens: SessionTokens? = null

        override fun saveTokens(tokens: SessionTokens) {
            this.tokens = tokens
        }

        override fun getTokens(): SessionTokens? = tokens

        override fun clearTokens() {
            tokens = null
        }
    }

    private class FakeAuthRepository : AuthRepository {

        var receivedRefreshToken: String? = null
        var refreshCalls = 0
        var shouldFail = false

        override suspend fun register(
            email: String,
            username: String,
            password: String
        ): RegisteredUser {
            error("Not used")
        }

        override suspend fun login(
            identifier: String,
            password: String
        ): AuthSession {
            error("Not used")
        }

        override suspend fun refresh(
            refreshToken: String
        ): AuthSession {
            refreshCalls++
            receivedRefreshToken = refreshToken

            if (shouldFail) {
                throw IOException("Network unavailable")
            }

            return AuthSession(
                accessToken = "new-access",
                refreshToken = "new-refresh"
            )
        }

        override suspend fun logout(refreshToken: String) {
            error("Not used")
        }
    }

    @Test
    fun `refresh replaces access and refresh tokens`() = runBlocking {
        val storage = FakeTokenStorage()
        val manager = SessionManager(storage)
        val repository = FakeAuthRepository()

        manager.saveSession("old-access", "old-refresh")

        val result = RefreshSessionUseCase(
            repository,
            manager
        )()

        assertTrue(result)
        assertEquals(
            "old-refresh",
            repository.receivedRefreshToken
        )
        assertEquals("new-access", manager.getAccessToken())
        assertEquals("new-refresh", manager.getRefreshToken())

        // Verify that the new tokens were persisted.
        val restoredManager = SessionManager(storage)
        assertTrue(restoredManager.restoreSession())
        assertEquals("new-access", restoredManager.getAccessToken())
        assertEquals("new-refresh", restoredManager.getRefreshToken())
    }

    @Test
    fun `refresh without session does not call backend`() = runBlocking {
        val manager = SessionManager(FakeTokenStorage())
        val repository = FakeAuthRepository()

        val result = RefreshSessionUseCase(
            repository,
            manager
        )()

        assertFalse(result)
        assertEquals(0, repository.refreshCalls)
    }

    @Test
    fun `network failure preserves existing tokens`() = runBlocking {
        val manager = SessionManager(FakeTokenStorage())
        val repository = FakeAuthRepository()

        manager.saveSession("old-access", "old-refresh")
        repository.shouldFail = true

        try {
            RefreshSessionUseCase(repository, manager)()
            fail("Expected IOException")
        } catch (_: IOException) {
            // Expected failure.
        }

        assertEquals("old-access", manager.getAccessToken())
        assertEquals("old-refresh", manager.getRefreshToken())
    }
}
