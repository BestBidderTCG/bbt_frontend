package com.gps_g6.bestbiddertcg.feature.profile.session

import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProfileManager(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) {

    private val mutex = Mutex()

    private val _profile = MutableStateFlow<Profile?>(null)

    val profile = _profile.asStateFlow()

    private var cachedAccessToken: String? = null

    suspend fun loadProfile(
        forceRefresh: Boolean = false
    ): Profile = mutex.withLock {

        val currentToken = requireAccessToken()

        val cachedProfile = _profile.value

        if (
            !forceRefresh &&
            cachedProfile != null &&
            cachedAccessToken == currentToken
        ) {
            return@withLock cachedProfile
        }

        // Never expose data cached for another session.
        clearCache()

        val loadedProfile = repository.getCurrentProfile()

        checkSessionUnchanged(currentToken)

        cachedAccessToken = currentToken
        _profile.value = loadedProfile

        loadedProfile
    }

    suspend fun updateProfile(
        username: String? = null,
        description: String? = null,
        avatarNum: Int? = null
    ): Profile = mutex.withLock {

        val currentToken = requireAccessToken()

        require(
            username != null ||
                    description != null ||
                    avatarNum != null
        ) {
            "At least one field must be provided"
        }

        // Do not retain another account's cached data.
        if (cachedAccessToken != currentToken) {
            clearCache()
        }

        val updatedProfile = repository.updateProfile(
            username = username,
            description = description,
            avatarNum = avatarNum
        )

        checkSessionUnchanged(currentToken)

        cachedAccessToken = currentToken
        _profile.value = updatedProfile

        updatedProfile
    }

    suspend fun clearProfile() = mutex.withLock {
        clearCache()
    }

    private fun requireAccessToken(): String {
        return sessionManager.getAccessToken()
            ?: throw IllegalStateException(
                "No authenticated session"
            )
    }

    private fun checkSessionUnchanged(
        expectedToken: String
    ) {
        if (sessionManager.getAccessToken() != expectedToken) {
            clearCache()

            throw IllegalStateException(
                "Authentication session changed"
            )
        }
    }

    private fun clearCache() {
        cachedAccessToken = null
        _profile.value = null
    }
}
