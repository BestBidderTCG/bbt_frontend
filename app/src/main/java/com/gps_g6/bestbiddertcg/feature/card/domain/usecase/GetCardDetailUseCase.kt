package com.gps_g6.bestbiddertcg.feature.card.domain.usecase

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

class GetCardDetailsUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(cardId: String): Card {
        return repository.getCardDetails(cardId)
    }
}
