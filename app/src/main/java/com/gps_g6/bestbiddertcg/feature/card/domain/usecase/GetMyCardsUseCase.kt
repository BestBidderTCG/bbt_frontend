package com.gps_g6.bestbiddertcg.feature.card.domain.usecase

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

// Caso de uso para obtener las cartas del usuario autenticado.
class GetMyCardsUseCase(
    private val repository: CardRepository
) {

    suspend operator fun invoke(): List<Card> {
        return repository.getMyCards()
    }
}