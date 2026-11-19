package com.gps_g6.bestbiddertcg.feature.auction.domain.model

data class AuctionSummary(
    val id: String,
    val cardName: String,
    val cardImage: String,
    val currentBid: Double,
    val remainingDays: Int,
    val cardState: String,
    val cardCategory: String,
    val cardCollection: String?,
    val cardNumber: Int?,
    val status: String
)
