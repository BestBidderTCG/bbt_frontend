package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus

data class MyCardDetailsUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: String,
    val collection: String?,
    val number: Int?,
    val condition: String,
    val description: String?,
    val publishedAt: String?,
    val availability: CardAvailability
)

sealed interface CardAvailability {

    data object Available : CardAvailability

    data class InAuction(
        val auctionId: String,
        val status: AuctionStatus
    ) : CardAvailability
}

sealed interface MyCardDetailsUiState {

    data object Loading : MyCardDetailsUiState

    data class Success(
        val card: MyCardDetailsUiModel
    ) : MyCardDetailsUiState

    data object Error : MyCardDetailsUiState
}
