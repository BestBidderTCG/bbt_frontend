package com.gps_g6.bestbiddertcg.feature.auction.data

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

// Implementa el repositorio y transforma los datos recibidos de la API.
class AuctionRepositoryImpl(
    private val api: AuctionApi
) : AuctionRepository {

    override suspend fun getAllAuctions(): List<Auction> {
        return api.getAllAuctions().map { it.toDomain() }
    }

    override suspend fun getActiveAuctions(): List<Auction> {
        return api.getActiveAuctions().map { it.toDomain() }
    }

    override suspend fun searchAuctions(
        query: String?,
        active: Boolean?,
        minPrice: Double?,
        maxPrice: Double?,
        page: Int,
        size: Int
    ): List<Auction> {
        return api.searchAuctions(
            query = query,
            active = active,
            minPrice = minPrice,
            maxPrice = maxPrice,
            page = page,
            size = size
        ).map { it.toDomain() }
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
    status = status
)