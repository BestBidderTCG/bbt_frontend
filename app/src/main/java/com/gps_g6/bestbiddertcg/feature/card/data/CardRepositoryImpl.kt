package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.data.remote.CardApi
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardDto
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardAuction
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

class CardRepositoryImpl(
    private val api: CardApi
) : CardRepository {

    override suspend fun getMyCards(): List<Card> {
        return api.getMyCards().map { it.toDomain() }
    }

    override suspend fun getCardDetails(cardId: String): Card {
        return api.getCardDetails(cardId).toDomain()
    }

    private fun CardDto.toDomain(): Card {
        return Card(
            id = id,
            ownerId = ownerId,
            name = name,
            image = image,
            category = category,
            collection = collection,
            num = num,
            state = state,
            description = description,
            createdAt = createdAt,
            auction = auction?.let {
                CardAuction(
                    id = it.id,
                    status = when (it.status) {
                        "IN_PROGRESS" -> AuctionStatus.ACTIVE
                        "UPCOMING" -> AuctionStatus.UPCOMING
                        "FINISHED" -> AuctionStatus.FINISHED
                        else -> throw IllegalArgumentException(
                            "Unknown auction status: ${it.status}"
                        )
                    }
                )
            }
        )
    }
}