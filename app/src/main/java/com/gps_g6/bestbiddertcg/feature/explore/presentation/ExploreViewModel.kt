
package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.currentCoroutineContext
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

    private var searchJob: Job? = null

    init {
        loadAuctions()
    }

    fun loadAuctions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _auctions.value = auctionRepository.getActiveAuctions()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "No se han podido cargar las subastas."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applyFilters(
        query: String?,
        active: Boolean?,
        minPrice: Double?,
        maxPrice: Double?
    ) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                delay(300)

                _auctions.value = auctionRepository.searchAuctions(
                    query = query?.trim()?.takeIf { it.isNotEmpty() },
                    active = active,
                    minPrice = minPrice,
                    maxPrice = maxPrice
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                exception.printStackTrace()
                _error.value = "No se han podido cargar las subastas."
            } finally {
                if (searchJob == currentCoroutineContext()[Job]) {
                    _isLoading.value = false
                }
            }
        }
    }
}
