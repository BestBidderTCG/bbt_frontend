package com.gps_g6.bestbiddertcg.feature.card.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardAuctionDto(
    val id: String,
    val status: String
)
