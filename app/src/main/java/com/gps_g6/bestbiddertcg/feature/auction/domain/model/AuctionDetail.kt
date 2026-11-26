package com.gps_g6.bestbiddertcg.feature.auction.domain.model

import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition

data class AuctionDetail(
    val id: String,
    val status: AuctionStatus,
    val cardName: String,
    val cardImage: String?,
    val cardCategory: CardCategory?,
    val cardCollection: CardCollection?,
    val cardNumber: Int?,
    val cardState: CardCondition,
    val minPrize: Double,
    val increment: Double,
    val lastBid: Double?,
    val bidCount: Long,
    val startedAt: Long,
    val endsAt: Long,
    val description: String?,
    val creatorId: String?,
    val creatorUsername: String?,
    val creatorAvatarNum: Int?,
    val isFavorite: Boolean
)