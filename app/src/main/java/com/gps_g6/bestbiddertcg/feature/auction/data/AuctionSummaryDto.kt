package com.gps_g6.bestbiddertcg.feature.auction.data

import kotlinx.serialization.Serializable

@Serializable
data class AuctionSummaryDto(
    val id: String,
    val cardName: String,
    val cardImage: String,
    val currentBid: Double,
    val remainingDays: Int,
    val cardState: String,
    val cardCategory: String,
    val cardCollection: String? = null,
    val cardNumber: Int? = null,
    val status: String
)