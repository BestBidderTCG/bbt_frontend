package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import java.time.LocalDateTime

/**
 * Estado de la pantalla de creación de subastas.
 */
data class CreateAuctionUiState(
    val cards: List<Card> = emptyList(),
    val isLoadingCards: Boolean = false,
    val cardsError: String? = null,
    val selectedCard: Card? = null,
    val startingPrice: String = "",
    val description: String = "",
    val startDateTime: LocalDateTime = defaultStartDateTime(),
    val durationInDays: Int = 3,
    val isCreating: Boolean = false,
    val errorMessage: String? = null,
    val createdAuction: Auction? = null
)