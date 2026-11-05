package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase

/**
 * Factory para crear instancias del ViewModel.
 */
class CreateAuctionViewModelFactory(
    private val createAuctionUseCase: CreateAuctionUseCase,
    private val getMyCardsUseCase: GetMyCardsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAuctionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateAuctionViewModel(
                createAuctionUseCase = createAuctionUseCase,
                getMyCardsUseCase = getMyCardsUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}