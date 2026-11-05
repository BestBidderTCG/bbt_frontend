
package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Gestiona los datos y estados de la pantalla Explore.
class ExploreViewModel(
    private val auctionRepository: AuctionRepository
) : ViewModel() {

    private val _auctions = MutableStateFlow<List<Auction>>(emptyList())
    val auctions: StateFlow<List<Auction>> = _auctions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAuctions()
    }

    // Obtiene las subastas activas desde el repositorio.
    fun loadAuctions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _auctions.value = auctionRepository.getActiveAuctions()
            } catch (exception: Exception) {
                exception.printStackTrace()
                _error.value = "No se han podido cargar las subastas."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
