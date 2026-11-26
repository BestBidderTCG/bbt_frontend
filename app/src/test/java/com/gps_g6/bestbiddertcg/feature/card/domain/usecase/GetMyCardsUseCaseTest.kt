package com.gps_g6.bestbiddertcg.feature.card.domain.usecase

import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class GetMyCardsUseCaseTest {

    private val repository = mockk<CardRepository>()
    private val useCase = GetMyCardsUseCase(repository)

    private val card = CardSummary(
        id = "card-1",
        name = "Piplup",
        image = "/uploads/piplup.jpg",
        category = CardCategory.POKEMON,
        inAuction = false
    )

    @Test
    fun `returns cards from repository`() = runBlocking {
        coEvery {
            repository.getMyCards()
        } returns listOf(card)

        val result = useCase()

        assertEquals(listOf(card), result)

        coVerify(exactly = 1) {
            repository.getMyCards()
        }
    }

    @Test
    fun `returns empty list when collection is empty`() = runBlocking {
        coEvery {
            repository.getMyCards()
        } returns emptyList()

        assertTrue(useCase().isEmpty())
    }

    @Test
    fun `propagates repository errors`() = runBlocking {
        coEvery {
            repository.getMyCards()
        } throws IOException("Network unavailable")

        try {
            useCase()
            fail("Expected IOException")
        } catch (_: IOException) {
            // El llamador gestiona el error.
        }

        coVerify(exactly = 1) {
            repository.getMyCards()
        }
    }
}