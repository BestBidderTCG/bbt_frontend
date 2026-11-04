
package com.gps_g6.bestbiddertcg.feature.auction.domain.model

// Representa una subasta dentro de la aplicación.
data class Auction(
    val id: String,
    val creatorId: String,
    val creatorUsername: String,
    val card: String,
    val collection: String,
    val minPrize: Double,
    val startedAt: String,
    val durationInDays: Int,
    val endsAt: String,
    val isCancelled: Boolean,
    val status: String
)
