package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

class CardRepositoryImpl(
    private val api: CardApi
) : CardRepository {

    override suspend fun getMyCards(): List<Card> {
        return api.getMyCards().map { dto ->
            Card(
                id = dto.id,
                ownerId = dto.ownerId,
                name = dto.name,
                image = dto.image,
                category = dto.category,
                collection = dto.collection,
                num = dto.num,
                state = dto.state,
                description = dto.description
            )
        }
    }
}