package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import java.time.LocalDateTime

/**
 * Datos de ejemplo para las previews de creación de subastas.
 */
object CreateAuctionSampleData {

    val uiState = CreateAuctionUiState(
        cards = emptyList(),
        isLoadingCards = false,
        cardsError = null,
        selectedCard = null,
        startingPrice = "10.00",
        description = "Subasta de ejemplo para la preview.",
        startDateTime = LocalDateTime.of(2026, 11, 10, 18, 0),
        durationInDays = 3,
        isCreating = false,
        errorMessage = null,
        createdAuction = null
    )
}