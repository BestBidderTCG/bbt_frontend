package com.gps_g6.bestbiddertcg.feature.auction.data

import kotlinx.serialization.Serializable

/**
 * DTO para crear una subasta.
 */
@Serializable
data class CreateAuctionRequestDto(
    val creator_id: String,
    val creator_username: String,
    val card_id: String,
    val description: String? = null,
    val min_prize: Double,
    val started_at: String,
    val duration_in_days: Int
)