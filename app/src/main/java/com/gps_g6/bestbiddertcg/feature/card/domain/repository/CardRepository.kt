package com.gps_g6.bestbiddertcg.feature.card.domain.repository

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary

interface CardRepository {
    suspend fun getMyCards(): List<CardSummary>
    suspend fun getCardDetails(cardId: String): Card
}
