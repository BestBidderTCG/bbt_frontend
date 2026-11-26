package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition

data class MyCardDetailsUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: CardCategory?,
    val collection: CardCollection?,
    val number: Int?,
    val condition: CardCondition,
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
