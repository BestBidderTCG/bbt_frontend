package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.feature.card.data.remote.CardApi
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardDto
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
    fun `getMyCards maps API response to domain cards`() = runBlocking {
        coEvery { api.getMyCards() } returns listOf(
            CardDto(
                id = "card-1",
                name = "Piplup",
                image = "/uploads/piplup.jpg",
                category = "Pokemon"
            ),
            CardDto(
                id = "card-2",
                name = "Charizard",
                image = "/uploads/charizard.jpg",
                category = "Pokemon"
            )
        )

        val result = repository.getMyCards()

        assertEquals(2, result.size)
        assertEquals("card-1", result[0].id)
        assertEquals("Piplup", result[0].name)
        assertEquals("/uploads/piplup.jpg", result[0].image)
        assertEquals("Pokemon", result[0].category)
        assertEquals("Charizard", result[1].name)

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
            // Network errors are handled by the ViewModel
        }

        coVerify(exactly = 1) {
            api.getMyCards()
        }
    }
}
