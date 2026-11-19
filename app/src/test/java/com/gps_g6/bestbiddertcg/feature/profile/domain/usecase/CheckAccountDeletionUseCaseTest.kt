package com.gps_g6.bestbiddertcg.feature.profile.domain.usecase

import com.gps_g6.bestbiddertcg.feature.profile.domain.model.AccountDeletionCheck
import com.gps_g6.bestbiddertcg.feature.profile.domain.repository.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class CheckAccountDeletionUseCaseTest {

    private val repository = mockk<ProfileRepository>()
    private val useCase = CheckAccountDeletionUseCase(repository)

    @Test
    fun `account without blockers can be deleted`() = runBlocking {
        coEvery { repository.checkAccountDeletion() } returns
                AccountDeletionCheck(
                    hasActiveAuction = false,
                    isWinningAuction = false
                )

        val result = useCase()

        assertTrue(result.canDelete)
        assertFalse(result.hasActiveAuction)
        assertFalse(result.isWinningAuction)

        coVerify(exactly = 1) {
            repository.checkAccountDeletion()
        }
    }

    @Test
    fun `account with blockers cannot be deleted`() = runBlocking {
        coEvery { repository.checkAccountDeletion() } returns
                AccountDeletionCheck(
                    hasActiveAuction = true,
                    isWinningAuction = true
                )

        val result = useCase()

        assertFalse(result.canDelete)
        assertTrue(result.hasActiveAuction)
        assertTrue(result.isWinningAuction)
    }

    @Test
    fun `repository error is propagated`() = runBlocking {
        coEvery {
            repository.checkAccountDeletion()
        } throws IOException("Network error")

        try {
            useCase()
            fail("Expected IOException")
        } catch (_: IOException) {
            // The caller handles network errors.
        }

        coVerify(exactly = 1) {
            repository.checkAccountDeletion()
        }
    }
}
