package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition

/**
 * Contiene los datos de la subasta.
 */
data class AuctionDetailUiState(
    val auctionId: String = "",
    val cardName: String = "",
    val cardImage: String? = null,
    val cardCategory: CardCategory? = null,
    val cardCollection: CardCollection? = null,
    val cardNumber: String = "",

    val status: AuctionStatus = AuctionStatus.ACTIVE,
    val isOwner: Boolean = false,
    val isWinningBid: Boolean = false,

    val currentBid: Double = 0.0,
    val startingPrice: Double = 0.0,
    val bidCount: Int = 0,
    val remainingTime: String = "",

    val cardCondition: CardCondition = CardCondition.NEW,
    val minimumIncrement: Double = 0.0,
    val publishedAt: String = "",
    val scheduledStart: String = "",
    val duration: String = "",

    val description: String = "",

    val sellerName: String = "",
    val sellerAvatar: Int = 0,

    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
