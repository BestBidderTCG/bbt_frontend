package com.gps_g6.bestbiddertcg.core.auth

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedTokenStorageTest {

    private lateinit var storage: EncryptedTokenStorage

    @Before
    fun setUp() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        storage = EncryptedTokenStorage(context)
        storage.clearTokens()
    }

    @After
    fun tearDown() {
        storage.clearTokens()
    }

    @Test
    fun tokensAreSavedAndRecovered() {
        val tokens = SessionTokens(
            accessToken = "test-access-token",
            refreshToken = "test-refresh-token"
        )

        storage.saveTokens(tokens)

        val recovered = storage.getTokens()

        assertNotNull(recovered)
        assertEquals(
            tokens.accessToken,
            recovered?.accessToken
        )
        assertEquals(
            tokens.refreshToken,
            recovered?.refreshToken
        )
    }

    @Test
    fun clearingTokensRemovesStoredCredentials() {
        storage.saveTokens(
            SessionTokens("access", "refresh")
        )

        storage.clearTokens()

        assertNull(storage.getTokens())
    }

    @Test
    fun emptyStorageReturnsNull() {
        assertNull(storage.getTokens())
    }
}
