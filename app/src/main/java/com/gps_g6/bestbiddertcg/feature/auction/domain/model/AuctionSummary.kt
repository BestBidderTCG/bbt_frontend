package com.gps_g6.bestbiddertcg.feature.auction.domain.model

import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition

data class AuctionSummary(
    val id: String,
    val cardName: String,
    val cardImage: String?,
    val currentBid: Double,
    val remainingDays: Int,
    val cardState: CardCondition,
    val cardCategory: CardCategory?,
    val cardCollection: CardCollection?,
    val cardNumber: Int?,
    val status: AuctionStatus
)