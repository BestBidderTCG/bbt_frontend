package com.gps_g6.bestbiddertcg.feature.card.domain.model

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus

data class CardAuction(
    val id: String,
    val status: AuctionStatus
)
