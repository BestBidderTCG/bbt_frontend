
package com.gps_g6.bestbiddertcg.feature.auction.data

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

// Implementa el repositorio y transforma los datos recibidos de la API.
class AuctionRepositoryImpl(
    private val api: AuctionApi
) : AuctionRepository {

    override suspend fun getActiveAuctions(): List<Auction> {
        return api.getActiveAuctions().map { auctionDto ->
            Auction(
                id = auctionDto.id,
                creatorId = auctionDto.creator_id,
                creatorUsername = auctionDto.creator_username,
                card = auctionDto.card,
                collection = auctionDto.collection,
                minPrize = auctionDto.min_prize,
                startedAt = auctionDto.started_at,
                durationInDays = auctionDto.duration_in_days,
                endsAt = auctionDto.ends_at,
                isCancelled = auctionDto.is_cancelled,
                status = auctionDto.status
            )
        }
    }
}
