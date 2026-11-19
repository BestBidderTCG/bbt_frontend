package com.gps_g6.bestbiddertcg.feature.card.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardDto(
    val id: String,
    val ownerId: String = "",
    val name: String,
    val image: String,
    val category: String,
    val collection: String? = null,
    val num: Int? = null,
    val state: String = "",
    val description: String? = null,
    val createdAt: String? = null,
    val auction: CardAuctionDto? = null
)