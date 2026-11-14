package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.feature.auction.data.AuctionModule
import com.gps_g6.bestbiddertcg.feature.card.data.CardModule
import com.gps_g6.bestbiddertcg.feature.profile.di.ProfileModule

@Composable
fun CreateAuctionRoute(
    onBackClick: () -> Unit = {},
    onAuctionCreated: () -> Unit = {}
) {
    val factory = remember {
        CreateAuctionViewModelFactory(
            createAuctionUseCase = AuctionModule.createAuctionUseCase,
            getMyCardsUseCase = CardModule.getMyCardsUseCase,
            profileManager = ProfileModule.profileManager
        )
    }

    val viewModel: CreateAuctionViewModel = viewModel(
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.start()
    }

    LaunchedEffect(uiState.createdAuction) {
        if (uiState.createdAuction != null) {
            viewModel.onAuctionCreatedHandled()
            onAuctionCreated()
        }
    }

    CreateAuctionScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onLoadCards = viewModel::loadCards,
        onCardSelected = viewModel::selectCard,
        onStartingPriceChange = viewModel::updateStartingPrice,
        onDescriptionChange = viewModel::updateDescription,
        onDurationSelected = viewModel::updateDuration,
        onStartDateTimeChange = viewModel::updateStartDateTime,
        onCreateAuction = viewModel::createAuction,
        onShowError = viewModel::showError
    )
}