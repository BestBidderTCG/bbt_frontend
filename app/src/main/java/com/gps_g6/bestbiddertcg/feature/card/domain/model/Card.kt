package com.gps_g6.bestbiddertcg.feature.card.domain.model

// Representa una carta de la colección del usuario.
data class Card(
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