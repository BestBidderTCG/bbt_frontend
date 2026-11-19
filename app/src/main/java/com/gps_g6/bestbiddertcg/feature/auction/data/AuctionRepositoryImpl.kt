package com.gps_g6.bestbiddertcg.feature.auction.data

import com.gps_g6.bestbiddertcg.core.network.NetworkModule.resolveImageUrl
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionDetail
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

// Implementa el repositorio y transforma los datos recibidos de la API.
class AuctionRepositoryImpl(
    private val api: AuctionApi
) : AuctionRepository {

    override suspend fun getAllAuctions(sort: AuctionSort): List<AuctionSummary> =
        api.getAllAuctions(sort.name).map {
            AuctionSummary(
                id = it.id, cardName = it.cardName, resolveImageUrl(it.cardImage),
                currentBid = it.currentBid, remainingDays = it.remainingDays,
                cardState = it.cardState, cardCategory = it.cardCategory,
                cardCollection = it.cardCollection, cardNumber = it.cardNumber,
                status = it.status,
            )
        }

    override suspend fun getAuctionById(auctionId: String): AuctionDetail {
        val response = api.getAuctionById(auctionId)

        return AuctionDetail(
            id = response.id,
            status = response.status,
            cardName = response.cardName,
            cardImage = resolveImageUrl(response.cardImage),
            cardCollection = response.cardCollection,
            cardNumber = response.cardNumber,
            cardState = response.cardState,
            minPrize = response.minPrize,
            increment = response.increment,
            lastBid = response.lastBid,
            bidCount = response.bidCount,
            startedAt = response.startedAt,
            endsAt = response.endsAt,
            description = response.description,
            creatorId = response.creator?.id,
            creatorUsername = response.creator?.username,
            creatorAvatarNum = response.creator?.avatarNum,
            isFavorite = response.isFavorite ?: false
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
    image = resolveImageUrl(card.image),
    category = card.category,
    condition = card.state
)