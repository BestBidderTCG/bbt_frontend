package com.gps_g6.bestbiddertcg.feature.auction.domain.model

/**
 * Datos de la subasta.
 */
data class CreateAuction(
    val creatorId: String,
    val creatorUsername: String,
    val cardId: String,
    val description: String?,
    val minPrize: Double,
    val startedAt: String,
    val durationInDays: Int
)