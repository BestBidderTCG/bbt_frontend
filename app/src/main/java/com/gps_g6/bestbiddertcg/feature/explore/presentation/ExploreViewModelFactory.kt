package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gps_g6.bestbiddertcg.feature.auction.data.AuctionModule

// Crea el ViewModel con las dependencias necesarias.
class ExploreViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // Comprueba que se solicita la clase ExploreViewModel.
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {

            // Suprime la advertencia de unchecked cast (por el tipo generico).
            @Suppress("UNCHECKED_CAST")

            // Crea y devuelve el ViewModel.
            return ExploreViewModel(
                auctionRepository = AuctionModule.auctionRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
