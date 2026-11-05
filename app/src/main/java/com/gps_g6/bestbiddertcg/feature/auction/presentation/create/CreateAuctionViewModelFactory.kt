package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager

/**
 * Factory para crear instancias del ViewModel.
 */
class CreateAuctionViewModelFactory(
    private val createAuctionUseCase: CreateAuctionUseCase,
    private val getMyCardsUseCase: GetMyCardsUseCase,
    private val profileManager: ProfileManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAuctionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateAuctionViewModel(
                createAuctionUseCase = createAuctionUseCase,
                getMyCardsUseCase = getMyCardsUseCase,
                profileManager = profileManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}