package com.gps_g6.bestbiddertcg.feature.profile.presentation

data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val cardsCount: Int = 0,
    val auctionsCount: Int = 0,
    val bidsCount: Int = 0,
    val collection: List<CollectionCardUiModel> = emptyList(),
    val auctions: List<AuctionUiModel> = emptyList()
)

data class CollectionCardUiModel(
    val id: String,
    val name: String
)

enum class AuctionStatus {
    ACTIVE,
    UPCOMING,
    FINISHED
}

data class AuctionUiModel(
    val id: String,
    val cardName: String,
    val price: String,
    val status: AuctionStatus,
    val detail: String
)

enum class ProfileTab {
    COLLECTION,
    AUCTIONS
}