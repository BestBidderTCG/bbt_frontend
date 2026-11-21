package com.gps_g6.bestbiddertcg.feature.card.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardSummaryDto(
    val id: String,
    val name: String,
    val image: String,
    val category: String,
    val inAuction: Boolean = false
)
