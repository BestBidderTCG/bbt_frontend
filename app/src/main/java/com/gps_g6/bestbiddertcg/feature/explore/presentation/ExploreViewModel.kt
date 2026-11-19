
package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Filtros de estado de subastas en la pantalla explore, los guardamos para que no se pierdan en el ordenado.
 */
private data class ExploreFilters(
    val query: String? = null,
    val endingWithinDays: Int? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val condition: String? = null,
    val collection: String? = null,
    val category: String? = null,
    val active: Boolean? = null
)

// Gestiona los datos y estados de la pantalla Explore.
class ExploreViewModel(
    private val auctionRepository: AuctionRepository
) : ViewModel() {

    private var allAuctions: List<AuctionSummary> = emptyList()
    private var filters = ExploreFilters()
    private val _auctions = MutableStateFlow<List<AuctionSummary>>(emptyList())
    val auctions: StateFlow<List<AuctionSummary>> = _auctions.asStateFlow()

    private val _sort = MutableStateFlow(AuctionSort.POPULAR)
    val sort: StateFlow<AuctionSort> = _sort.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAuctions()
    }

    fun loadAuctions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val apiSort = if (_sort.value == AuctionSort.POPULAR) {
                    AuctionSort.ENDING_SOON
                } else {
                    _sort.value
                }

                allAuctions = auctionRepository.getAllAuctions(apiSort)
                _auctions.value = filterAuctions(allAuctions)
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

    fun changeSort(newSort: AuctionSort) {
        if (newSort == _sort.value) return

        _sort.value = newSort

        if (newSort != AuctionSort.POPULAR) {
            loadAuctions()
        }
    }

    fun applyFilters(
        query: String? = filters.query,
        active: Boolean? = filters.active,
        endingWithinDays: Int? = filters.endingWithinDays,
        minPrice: Double? = filters.minPrice,
        maxPrice: Double? = filters.maxPrice,
        condition: String? = filters.condition,
        collection: String? = filters.collection,
        category: String? = filters.category
    ) {
        filters = ExploreFilters(
            query = query?.trim()?.takeIf { it.isNotEmpty() },
            active = active,
            endingWithinDays = endingWithinDays,
            minPrice = minPrice,
            maxPrice = maxPrice,
            condition = condition?.trim()?.takeIf { it.isNotEmpty() },
            collection = collection?.trim()?.takeIf { it.isNotEmpty() && it != "Todas" },
            category = category?.trim()?.takeIf { it.isNotEmpty() && it != "Todas" }
        )
        _auctions.value = filterAuctions(allAuctions)
    }

    fun clearFilters() {
        filters = ExploreFilters()
        loadAuctions()
    }

    private fun filterAuctions(auctions: List<AuctionSummary>): List<AuctionSummary> {
        val f = filters
        return auctions.filter { auction ->
            (f.query == null || auction.cardName.contains(f.query, ignoreCase = true)) &&
                    (f.minPrice == null || auction.currentBid >= f.minPrice) &&
                    (f.maxPrice == null || auction.currentBid <= f.maxPrice) &&
                    (f.condition == null || auction.cardState.equals(f.condition, ignoreCase = true)) &&
                    (f.collection == null || auction.cardCollection.equals(f.collection, ignoreCase = true)) &&
                    (f.category == null || auction.cardCategory.equals(f.category, ignoreCase = true)) &&
                    (f.active == null || auction.status.equals(if (f.active) "IN_PROGRESS" else "UPCOMING", ignoreCase = true)) &&
                    (f.endingWithinDays == null || auction.remainingDays <= maxOf(f.endingWithinDays, 1))
        }
    }
}
