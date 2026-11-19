package com.gps_g6.bestbiddertcg.feature.auction.domain.usecase

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionDetail
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository

class GetAuctionDetailUseCase(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(auctionId: String): AuctionDetail {
        return repository.getAuctionById(auctionId)
    }
}