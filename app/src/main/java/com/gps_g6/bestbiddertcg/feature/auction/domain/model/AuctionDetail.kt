package com.gps_g6.bestbiddertcg.feature.auction.domain.model

data class AuctionDetail(
    val id: String,
    val status: String,
    val cardName: String,
    val cardImage: String,
    val cardCollection: String?,
    val cardNumber: Int?,
    val cardState: String,
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