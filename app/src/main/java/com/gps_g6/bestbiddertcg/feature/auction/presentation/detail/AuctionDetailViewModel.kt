
package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionDetail
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.GetAuctionDetailUseCase
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AuctionDetailViewModel(
    private val getAuctionDetailUseCase: GetAuctionDetailUseCase,
    private val profileManager: ProfileManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuctionDetailUiState(isLoading = true))
    val uiState: StateFlow<AuctionDetailUiState> = _uiState.asStateFlow()

    fun loadAuction(auctionId: String) {
        if (auctionId.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "El identificador de la subasta no es válido."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                val detail = getAuctionDetailUseCase(auctionId)
                val currentProfile = runCatching {
                    profileManager.loadProfile()
                }.getOrNull()

                _uiState.value = detail.toUiState(
                    currentUserId = currentProfile?.id
                )
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se ha podido cargar el detalle de la subasta."
                    )
                }
            }
        }
    }

    private fun AuctionDetail.toUiState(
        currentUserId: String?
    ): AuctionDetailUiState {
        val startInstant = Instant.ofEpochMilli(startedAt)
        val endInstant = Instant.ofEpochMilli(endsAt)
        val now = Instant.now()

        val isUpcoming = status == AuctionStatus.UPCOMING
        val isOwner = creatorId != null &&
                currentUserId != null &&
                creatorId == currentUserId

        val remainingTime = if (isUpcoming) {
            formatDuration(startInstant.toEpochMilli() - now.toEpochMilli())
        } else {
            formatDuration(endInstant.toEpochMilli() - now.toEpochMilli())
        }

        return AuctionDetailUiState(
            auctionId = id,
            cardName = cardName,
            cardImage = cardImage,
            cardCategory = cardCategory,
            cardCollection = cardCollection,
            cardNumber = cardNumber?.toString().orEmpty(),
            status = status,
            isOwner = isOwner,
            currentBid = lastBid ?: minPrize,
            startingPrice = minPrize,
            bidCount = bidCount.toInt(),
            remainingTime = remainingTime,
            cardCondition = cardState,
            minimumIncrement = increment,
            publishedAt = formatDate(startInstant),
            scheduledStart = formatDate(startInstant),
            duration = formatAuctionDuration(endsAt - startedAt),
            description = description.orEmpty(),
            sellerName = creatorUsername.orEmpty(),
            sellerAvatar = creatorAvatarNum ?: 0,
            isFavorite = isFavorite,
            isLoading = false,
            errorMessage = null
        )
    }

    private fun formatDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault())

        return formatter.format(instant)
    }

    private fun formatDuration(milliseconds: Long): String {
        if (milliseconds <= 0) return "00:00:00"

        val totalSeconds = milliseconds / 1000
        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (days > 0) {
            "${days}d ${hours}h ${minutes}m"
        } else {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        }
    }

    private fun formatAuctionDuration(milliseconds: Long): String {
        val days = (milliseconds / 86_400_000).toInt()

        return when (days) {
            1 -> "1 día"
            in 2..7 -> "$days días"
            else -> "Duración no válida"
        }
    }
}
