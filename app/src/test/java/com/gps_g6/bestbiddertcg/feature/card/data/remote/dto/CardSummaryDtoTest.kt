package com.gps_g6.bestbiddertcg.feature.card.data.remote.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class CardSummaryDtoTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `missing inAuction defaults to false`() {
        val response = """
            {
                "id": "card-1",
                "name": "Piplup",
                "image": "/uploads/piplup.jpg",
                "category": "Pokémon"
            }
        """.trimIndent()

        val card = json.decodeFromString<CardSummaryDto>(response)

        assertFalse(card.inAuction)
        assertEquals("card-1", card.id)
        assertEquals("Piplup", card.name)
    }

    @Test
    fun `inAuction true is preserved`() {
        val response = """
            {
                "id": "card-1",
                "name": "Piplup",
                "image": "/uploads/piplup.jpg",
                "category": "Pokémon",
                "inAuction": true
            }
        """.trimIndent()

        val card = json.decodeFromString<CardSummaryDto>(response)

        assertTrue(card.inAuction)
    }

    @Test
    fun `inAuction false is preserved`() {
        val response = """
            {
                "id": "card-1",
                "name": "Piplup",
                "image": "/uploads/piplup.jpg",
                "category": "Pokémon",
                "inAuction": false
            }
        """.trimIndent()

        val card = json.decodeFromString<CardSummaryDto>(response)

        assertFalse(card.inAuction)
    }
}
