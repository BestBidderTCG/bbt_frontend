package com.gps_g6.bestbiddertcg.feature.card.domain.model

// Representa una carta de la colección del usuario.
data class Card(
    val id: String,
    val ownerId: String,
    val name: String,
    val image: String,
    val category: CardCategory?,
    val collection: CardCollection?,
    val num: Int?,
    val state: CardCondition,
    val description: String?,
    val createdAt: String? = null,
    val auction: CardAuction? = null
)