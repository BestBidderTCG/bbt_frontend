package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.data.remote.CardApi
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardAuctionDto
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardDto
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardSummaryDto
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class CardRepositoryImplTest {

    private val api = mockk<CardApi>()
    private val repository = CardRepositoryImpl(api)

    @Test
    fun `getMyCards maps API response to card summaries`() = runBlocking {
        coEvery { api.getMyCards() } returns listOf(
            CardSummaryDto(
                id = "card-1",
                name = "Piplup",
                image = "/uploads/piplup.jpg",
                category = "Pokémon",
                inAuction = false
            ),
            CardSummaryDto(
                id = "card-2",
                name = "Mox Diamond",
                image = "/uploads/mox.jpg",
                category = "Magic",
                inAuction = true
            )
        )

        val result = repository.getMyCards()

        assertEquals(2, result.size)

        assertEquals("card-1", result[0].id)
        assertEquals("Piplup", result[0].name)
        assertEquals("/uploads/piplup.jpg", result[0].image)
        assertEquals(CardCategory.POKEMON, result[0].category)
        assertFalse(result[0].inAuction)

        assertEquals("card-2", result[1].id)
        assertEquals("Mox Diamond", result[1].name)
        assertEquals(CardCategory.MAGIC, result[1].category)
        assertTrue(result[1].inAuction)

        coVerify(exactly = 1) {
            api.getMyCards()
        }
    }

    @Test
    fun `getMyCards returns empty collection`() = runBlocking {
        coEvery { api.getMyCards() } returns emptyList()

        val result = repository.getMyCards()

        assertTrue(result.isEmpty())

        coVerify(exactly = 1) {
            api.getMyCards()
        }
    }

    @Test
    fun `getMyCards propagates network errors`() = runBlocking {
        coEvery {
            api.getMyCards()
        } throws IOException("Connection failed")

        try {
            repository.getMyCards()
            fail("Expected IOException")
        } catch (_: IOException) {
            // El ViewModel gestiona los errores de red
        }

        coVerify(exactly = 1) {
            api.getMyCards()
        }
    }

    @Test
    fun `getCardDetails maps full card correctly`() = runBlocking {
        coEvery {
            api.getCardDetails("card-1")
        } returns CardDto(
            id = "card-1",
            ownerId = "user-1",
            name = "Piplup",
            image = "/uploads/piplup.jpg",
            category = "Pokémon",
            collection = "Surging Sparks",
            num = 393,
            state = "GOOD",
            description = "Carta de prueba",
            createdAt = "2026-11-21T17:10:54Z",
            auction = null
        )

        val result = repository.getCardDetails("card-1")

        assertEquals("card-1", result.id)
        assertEquals("user-1", result.ownerId)
        assertEquals("Piplup", result.name)
        assertEquals(CardCategory.POKEMON, result.category)
        assertEquals(CardCollection.SURGING_SPARKS, result.collection)
        assertEquals(393, result.num)
        assertEquals(CardCondition.GOOD, result.state)
        assertEquals("Carta de prueba", result.description)
        assertEquals("2026-11-21T17:10:54Z", result.createdAt)
        assertNull(result.auction)

        coVerify(exactly = 1) {
            api.getCardDetails("card-1")
        }
    }

    @Test
    fun `getCardDetails maps active auction correctly`() = runBlocking {
        coEvery {
            api.getCardDetails("card-1")
        } returns CardDto(
            id = "card-1",
            ownerId = "user-1",
            name = "Piplup",
            image = "/uploads/piplup.jpg",
            category = "Pokémon",
            state = "NEAR_MINT",
            auction = CardAuctionDto(
                id = "auction-1",
                status = "IN_PROGRESS"
            )
        )

        val result = repository.getCardDetails("card-1")

        assertEquals("auction-1", result.auction?.id)
        assertEquals(AuctionStatus.ACTIVE, result.auction?.status)
        assertEquals(CardCondition.NEAR_MINT, result.state)
    }

    @Test
    fun `getCardDetails propagates network errors`() = runBlocking {
        coEvery {
            api.getCardDetails("card-1")
        } throws IOException("Network unavailable")

        try {
            repository.getCardDetails("card-1")
            fail("Expected IOException")
        } catch (_: IOException) {
            // El ViewModel gestiona los errores
        }

        coVerify(exactly = 1) {
            api.getCardDetails("card-1")
        }
    }

}