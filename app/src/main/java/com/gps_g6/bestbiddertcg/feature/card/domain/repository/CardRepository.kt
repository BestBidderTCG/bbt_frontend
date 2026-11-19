package com.gps_g6.bestbiddertcg.feature.card.domain.repository

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card

interface CardRepository {
    suspend fun getMyCards(): List<Card>
    suspend fun getCardDetails(cardId: String): Card
}
