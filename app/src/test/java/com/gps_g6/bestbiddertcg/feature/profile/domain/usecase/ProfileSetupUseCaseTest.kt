package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.core.auth.SessionTokens
import com.gps_g6.bestbiddertcg.core.auth.TokenStorage
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileSetupUseCaseTest {

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
        var receivedDescription: String? = null
        var receivedAvatarNum: Int? = null
        var updateCalls = 0

        override suspend fun getCurrentProfile(): Profile =
            Profile(
                id = "test-id",
                username = "testuser",
                email = "test@example.com",
                description = null,
                avatarNum = 1,
                createdAt = "2026-11-05T12:00:00"
            )

        override suspend fun updateProfile(
            username: String?,
            description: String?,
            avatarNum: Int?
        ): Profile {
            updateCalls++
            receivedDescription = description
            receivedAvatarNum = avatarNum

            return getCurrentProfile().copy(
                description = description,
                avatarNum = avatarNum ?: 1
            )
        }
    }

    private fun createManager(
        repository: ProfileRepository
    ): ProfileManager {
        val session = SessionManager(FakeTokenStorage())

        session.saveSession(
            "test-access-token",
            "test-refresh-token"
        )

        return ProfileManager(repository, session)
    }

    @Test
    fun `saves description and selected avatar`() = runBlocking {
        val repository = FakeProfileRepository()
        val manager = createManager(repository)
        val useCase = ProfileSetupUseCase(manager)

        useCase(
            description = "  Colecciono cartas TCG  ",
            avatarNum = 4
        )

        assertEquals(1, repository.updateCalls)
        assertEquals(
            "Colecciono cartas TCG",
            repository.receivedDescription
        )
        assertEquals(4, repository.receivedAvatarNum)
        assertEquals(4, manager.profile.value?.avatarNum)
    }

    @Test
    fun `uses default avatar when none is selected`() = runBlocking {
        val repository = FakeProfileRepository()
        val manager = createManager(repository)
        val useCase = ProfileSetupUseCase(manager)

        useCase(
            description = "",
            avatarNum = null
        )

        assertEquals(1, repository.updateCalls)
        assertEquals(null, repository.receivedDescription)
        assertEquals(1, repository.receivedAvatarNum)
    }
}
