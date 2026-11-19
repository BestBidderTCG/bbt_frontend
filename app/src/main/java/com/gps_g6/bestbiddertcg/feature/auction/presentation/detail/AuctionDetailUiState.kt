package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

/**
 * Representa el estado de la subasta.
 */
enum class AuctionDetailStatus {
    ACTIVE,
    UPCOMING
}

/**
 * Contiene los datos de la subasta.
 */
data class AuctionDetailUiState(
    val auctionId: String = "",
    val cardName: String = "",
    val cardImage: String = "",
    val cardCategory: String = "",
    val cardCollection: String = "",
    val cardNumber: String = "",

    val status: AuctionDetailStatus = AuctionDetailStatus.ACTIVE,
    val isOwner: Boolean = false,

    val currentBid: Double = 0.0,
    val startingPrice: Double = 0.0,
    val bidCount: Int = 0,
    val remainingTime: String = "",

    val cardCondition: String = "",
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
