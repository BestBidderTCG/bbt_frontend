package com.gps_g6.bestbiddertcg.feature.auction.data

import com.gps_g6.bestbiddertcg.feature.auction.data.remote.AuctionApi
import com.gps_g6.bestbiddertcg.feature.auction.data.remote.dto.AuctionResponseDto
import com.gps_g6.bestbiddertcg.feature.auction.data.remote.dto.CreateAuctionRequestDto
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

// Implementa el repositorio y transforma los datos recibidos de la API.
class AuctionRepositoryImpl(
    private val api: AuctionApi
) : AuctionRepository {

    override suspend fun getAllAuctions(sort: AuctionSort): List<AuctionSummary> =
        api.getAllAuctions(sort.name).map {
            AuctionSummary(
                id = it.id, cardName = it.cardName, cardImage = it.cardImage,
                currentBid = it.currentBid, remainingDays = it.remainingDays,
                cardState = it.cardState, cardCategory = it.cardCategory,
                cardCollection = it.cardCollection, cardNumber = it.cardNumber,
                status = it.status,
            )
        }

    override suspend fun createAuction(request: CreateAuction): Auction {
        val response = api.createAuction(
            CreateAuctionRequestDto(
                creator_id = request.creatorId,
                creator_username = request.creatorUsername,
                card_id = request.cardId,
                description = request.description,
                min_prize = request.minPrize,
                started_at = request.startedAt,
                duration_in_days = request.durationInDays
            )
        )
        return response.toDomain()
    }
}

private fun AuctionResponseDto.toDomain(): Auction = Auction(
    id = id,
    creatorId = creator_id,
    creatorUsername = creator_username,
    card = card.name,
    collection = card.collection.orEmpty(),
    minPrize = min_prize,
    startedAt = startedAt,
    durationInDays = durationInDays,
    endsAt = endsAt,
    isCancelled = status == "CANCELLED",
    status = status,
    image = card.image,
    category = card.category,
    condition = card.state
)