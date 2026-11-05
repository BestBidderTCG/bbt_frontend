package com.gps_g6.bestbiddertcg.feature.auction.data

import kotlinx.serialization.Serializable

/**
 * Datos de la subasta.
 */
@Serializable
data class AuctionResponseDto(
    val id: String,
    val creator_id: String,
    val creator_username: String,
    val card: AuctionCardDto,
    val description: String?,
    val min_prize: Double,
    val startedAt: String,
    val endsAt: String,
    val durationInDays: Int,
    val status: String
)

/**
 * Datos de la carta que se subasta.
 */
@Serializable
data class AuctionCardDto(
    val id: String,
    val ownerId: String,
    val name: String,
    val image: String,
    val category: String,
    val collection: String?,
    val num: Int?,
    val state: String,
    val description: String?
)