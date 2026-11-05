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

class LogoutUseCaseTest {

    private class FakeStorage : TokenStorage {
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
        var shouldFail = false

        override suspend fun register(
            email: String,
            username: String,
            password: String
        ): RegisteredUser {
            error("Not used in this test")
        }

        override suspend fun login(
            identifier: String,
            password: String
        ): AuthSession {
            error("Not used in this test")
        }

        override suspend fun refresh(
            refreshToken: String
        ): AuthSession {
            error("Not used in this test")
        }

        override suspend fun logout(refreshToken: String) {
            receivedRefreshToken = refreshToken

            if (shouldFail) {
                throw IOException("Network unavailable")
            }
        }
    }

    @Test
    fun `logout revokes token and clears local session`() = runBlocking {
        val storage = FakeStorage()
        val manager = SessionManager(storage)
        val repository = FakeAuthRepository()

        manager.saveSession("access-test", "refresh-test")

        LogoutUseCase(repository, manager)()

        assertEquals(
            "refresh-test",
            repository.receivedRefreshToken
        )
        assertFalse(manager.isAuthenticated())
        assertNull(storage.getTokens())
    }

    @Test
    fun `logout clears local session when backend fails`() = runBlocking {
        val storage = FakeStorage()
        val manager = SessionManager(storage)
        val repository = FakeAuthRepository()

        repository.shouldFail = true
        manager.saveSession("access-test", "refresh-test")

        LogoutUseCase(repository, manager)()

        assertFalse(manager.isAuthenticated())
        assertNull(storage.getTokens())
    }
}
