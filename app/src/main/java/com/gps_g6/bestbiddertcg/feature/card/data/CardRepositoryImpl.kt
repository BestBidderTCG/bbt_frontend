package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.data.remote.CardApi
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardDto
import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardSummaryDto
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardAuction
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository

class CardRepositoryImpl(
    private val api: CardApi
) : CardRepository {

    override suspend fun getMyCards(): List<CardSummary> {
        return api.getMyCards().map { it.toSummary() }
    }

    override suspend fun getCardDetails(cardId: String): Card {
        return api.getCardDetails(cardId).toDomain()
    }

    private fun CardSummaryDto.toSummary(): CardSummary =
        CardSummary(
            id = id,
            name = name,
            image = image,
            category = category
                .takeIf { it.isNotBlank() }
                ?.let(CardCategory::fromApi)
        )

    private fun CardDto.toDomain(): Card =
        Card(
            id = id,
            ownerId = ownerId,
            name = name,
            image = image,
            category = category
                .takeIf { it.isNotBlank() }
                ?.let(CardCategory::fromApi),
            collection = collection
                ?.takeIf { it.isNotBlank() }
                ?.let(CardCollection::fromApi),
            num = num,
            state = CardCondition.fromApi(state),
            description = description,
            createdAt = createdAt,
            auction = auction?.let {
                CardAuction(
                    id = it.id,
                    status = AuctionStatus.fromBackend(it.status)
                )
            }
        )
}
