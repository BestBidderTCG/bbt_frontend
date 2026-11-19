package com.gps_g6.bestbiddertcg.feature.auction.data

import kotlinx.serialization.Serializable

@Serializable
data class AuctionDetailDto(
    val id: String,
    val status: String,
    val cardName: String,
    val cardImage: String,
    val cardCollection: String? = null,
    val cardNumber: Int? = null,
    val cardState: String,
    val minPrize: Double,
    val increment: Double,
    val lastBid: Double? = null,
    val bidCount: Long,
    val startedAt: Long,
    val endsAt: Long,
    val description: String? = null,
    val creator: AuctionCreatorDto? = null,
    val isFavorite: Boolean? = null
)

@Serializable
data class AuctionCreatorDto(
    val id: String,
    val username: String,
    val avatarNum: Int? = null
)
