package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.feature.card.data.CardModule

@Composable
fun MyCardDetailsRoute(
    cardId: String,
    onBackClick: () -> Unit,
    onAuctionClick: (String) -> Unit,
    onEditCardClick: (String) -> Unit,
    onCreateAuctionClick: (String) -> Unit,
    onDeleteCardClick: (String) -> Unit
) {
    val factory = remember(cardId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return MyCardDetailsViewModel(
                    cardId = cardId,
                    getCardDetailsUseCase =
                        CardModule.getCardDetailsUseCase
                ) as T
            }
        }
    }

    val viewModel: MyCardDetailsViewModel = viewModel(
        key = "card-details-$cardId",
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    MyCardDetailsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetryClick = viewModel::loadCard,
        onAuctionClick = onAuctionClick,
        onEditCardClick = { onEditCardClick(cardId) },
        onCreateAuctionClick = { onCreateAuctionClick(cardId) },
        onDeleteCardClick = { onDeleteCardClick(cardId) }
    )
}
