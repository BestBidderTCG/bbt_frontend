package com.gps_g6.bestbiddertcg.feature.profile.session

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.auth.SessionTokens
import com.gps_g6.bestbiddertcg.core.auth.TokenStorage
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ProfileManagerTest {

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

    private class FakeProfileRepository : ProfileRepository {

        var getCalls = 0
        var updateCalls = 0

        var currentProfile = Profile(
            id = "user-1",
            username = "testuser",
            email = "test@example.com",
            description = null,
            avatarNum = 1,
            createdAt = "2026-11-05T12:00:00"
        )

        override suspend fun getCurrentProfile(): Profile {
            getCalls++
            return currentProfile
        }

        override suspend fun updateProfile(
            username: String?,
            description: String?,
            avatarNum: Int?
        ): Profile {
            updateCalls++

            currentProfile = currentProfile.copy(
                username = username ?: currentProfile.username,
                description = description ?: currentProfile.description,
                avatarNum = avatarNum ?: currentProfile.avatarNum
            )

            return currentProfile
        }
    }

    @Test
    fun `first load fetches profile from backend`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        val profile = manager.loadProfile()

        assertEquals(1, repository.getCalls)
        assertEquals("testuser", profile.username)
        assertEquals(profile, manager.profile.value)
    }

    @Test
    fun `second load uses cached profile`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        manager.loadProfile()
        manager.loadProfile()

        assertEquals(1, repository.getCalls)
    }

    @Test
    fun `force refresh fetches profile again`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        manager.loadProfile()
        manager.loadProfile(forceRefresh = true)

        assertEquals(2, repository.getCalls)
    }

    @Test
    fun `updating profile refreshes cached data`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        manager.loadProfile()

        manager.updateProfile(
            description = "Colecciono cartas",
            avatarNum = 4
        )

        assertEquals(1, repository.updateCalls)
        assertEquals(
            "Colecciono cartas",
            manager.profile.value?.description
        )
        assertEquals(4, manager.profile.value?.avatarNum)

        manager.loadProfile()

        assertEquals(1, repository.getCalls)
    }

    @Test
    fun `changing session invalidates cached profile`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        manager.loadProfile()

        repository.currentProfile = repository.currentProfile.copy(
            id = "user-2",
            username = "anotheruser",
            email = "another@example.com"
        )

        session.saveSession("access-2", "refresh-2")

        val profile = manager.loadProfile()

        assertEquals(2, repository.getCalls)
        assertEquals("anotheruser", profile.username)
    }

    @Test
    fun `clearing profile removes cached data`() = runBlocking {
        val repository = FakeProfileRepository()
        val session = SessionManager(FakeTokenStorage())

        session.saveSession("access-1", "refresh-1")

        val manager = ProfileManager(repository, session)

        manager.loadProfile()
        manager.clearProfile()

        assertNull(manager.profile.value)
    }
}
