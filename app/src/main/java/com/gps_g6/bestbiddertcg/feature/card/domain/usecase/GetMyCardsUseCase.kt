package com.gps_g6.bestbiddertcg.feature.card.domain.usecase

import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

class GetMyCardsUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(): List<CardSummary> =
        repository.getMyCards()
}
