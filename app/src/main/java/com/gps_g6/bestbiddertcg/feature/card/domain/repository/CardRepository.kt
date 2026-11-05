package com.gps_g6.bestbiddertcg.feature.card.domain.repository

import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card

interface CardRepository {

    // Obtiene las cartas del usuario autenticado.
    suspend fun getMyCards(): List<Card>
}