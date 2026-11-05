package com.gps_g6.bestbiddertcg.feature.auction.domain.usecase

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

// Caso de uso para crear una nueva subasta.
class CreateAuctionUseCase(
    private val repository: AuctionRepository
) {

    suspend operator fun invoke(request: CreateAuction): Auction {
        return repository.createAuction(request)
    }
}