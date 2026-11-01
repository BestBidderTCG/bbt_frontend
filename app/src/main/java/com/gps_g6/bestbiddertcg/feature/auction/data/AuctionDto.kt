package com.gps_g6.bestbiddertcg.feature.auction.data

import kotlinx.serialization.Serializable

// Representa los datos de una subasta recibidos desde el backend.
@Serializable
data class AuctionDto(
    val id: String,
    val creator_id: String,
    val creator_username: String,
    val card: String,
    val collection: String,
    val min_prize: Double,
    val started_at: String,
    val duration_in_days: Int,
    val ends_at: String,
    val is_cancelled: Boolean,
    val status: String
)