package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.GetAuctionDetailUseCase
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager

class AuctionDetailViewModelFactory(
    private val getAuctionDetailUseCase: GetAuctionDetailUseCase,
    private val profileManager: ProfileManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuctionDetailViewModel(
                getAuctionDetailUseCase = getAuctionDetailUseCase,
                profileManager = profileManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
