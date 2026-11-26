package com.gps_g6.bestbiddertcg.feature.card.domain.usecase

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class GetCardDetailsUseCaseTest {

    private val repository = mockk<CardRepository>()
    private val useCase = GetCardDetailsUseCase(repository)

    private val card = Card(
        id = "card-1",
        ownerId = "user-1",
        name = "Piplup",
        image = "/uploads/piplup.jpg",
        category = CardCategory.POKEMON,
        collection = CardCollection.POKEMON_151,
        num = 393,
        state = CardCondition.GOOD,
        description = "Carta de prueba",
        createdAt = "2026-11-21T17:10:54Z",
        auction = null
    )

    @Test
    fun `returns card details from repository`() = runBlocking {
        coEvery {
            repository.getCardDetails("card-1")
        } returns card

        val result = useCase("card-1")

        assertEquals(card, result)

        coVerify(exactly = 1) {
            repository.getCardDetails("card-1")
        }
    }

    @Test
    fun `propagates repository errors`() = runBlocking {
        coEvery {
            repository.getCardDetails("card-1")
        } throws IOException("Network unavailable")

        try {
            useCase("card-1")
            fail("Expected IOException")
        } catch (_: IOException) {
            // El ViewModel gestiona los errores
        }

        coVerify(exactly = 1) {
            repository.getCardDetails("card-1")
        }
    }
}
