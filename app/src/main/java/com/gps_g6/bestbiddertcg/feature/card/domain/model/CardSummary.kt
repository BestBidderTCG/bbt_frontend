package com.gps_g6.bestbiddertcg.feature.card.domain.model

data class CardSummary(
    val id: String,
    val name: String,
    val image: String,
    val category: CardCategory?,
    val inAuction: Boolean
)
