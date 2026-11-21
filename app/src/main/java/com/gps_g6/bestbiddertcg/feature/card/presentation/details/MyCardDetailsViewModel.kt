package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetCardDetailsUseCase
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyCardDetailsViewModel(
    private val cardId: String,
    private val getCardDetailsUseCase: GetCardDetailsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<MyCardDetailsUiState>(
            MyCardDetailsUiState.Loading
        )

    val uiState = _uiState.asStateFlow()

    init {
        loadCard()
    }

    fun loadCard() {
        viewModelScope.launch {
            _uiState.value = MyCardDetailsUiState.Loading

            try {
                val card = getCardDetailsUseCase(cardId)

                val availability = when (card.auction?.status) {
                    AuctionStatus.ACTIVE,
                    AuctionStatus.UPCOMING -> CardAvailability.InAuction(
                        auctionId = card.auction.id,
                        status = card.auction.status
                    )

                    else -> CardAvailability.Available
                }

                _uiState.value = MyCardDetailsUiState.Success(
                    MyCardDetailsUiModel(
                        id = card.id,
                        name = card.name,
                        imageUrl = NetworkModule.resolveImageUrl(card.image),
                        category = card.category,
                        collection = card.collection,
                        number = card.num,
                        condition = card.state,
                        description = card.description,
                        publishedAt = card.createdAt,
                        availability = availability
                    )
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = MyCardDetailsUiState.Error
            }
        }
    }
}
