package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * ViewModel para la creación de subastas.
 */
class CreateAuctionViewModel(
    private val createAuctionUseCase: CreateAuctionUseCase,
    private val getMyCardsUseCase: GetMyCardsUseCase
) : ViewModel() {

    private companion object {
        // TODO: sustituir por el usuario autenticado cuando exista el login.
        // Debe coincidir con el usuario cuyas cartas se listan (usuarios de prueba del backend).
        const val TEMP_CREATOR_ID = "11111111-1111-1111-1111-111111111111"
        const val TEMP_CREATOR_USERNAME = "user1"
    }

    private val _uiState = MutableStateFlow(CreateAuctionUiState())
    val uiState: StateFlow<CreateAuctionUiState> = _uiState.asStateFlow()

    /**
     * Reinicia el formulario y carga las cartas del usuario.
     * Se llama cada vez que se abre la pantalla.
     */
    fun start() {
        _uiState.value = CreateAuctionUiState(isLoadingCards = true)
        loadCards()
    }

    /**
     * Carga las cartas de la colección del usuario.
     */
    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCards = true, cardsError = null) }

            try {
                // TODO: revisar
                val cards = getMyCardsUseCase().filter {
                    it.ownerId.equals(TEMP_CREATOR_ID, ignoreCase = true)
                }
                _uiState.update { it.copy(cards = cards, isLoadingCards = false) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingCards = false,
                        cardsError = "No se han podido cargar tus cartas."
                    )
                }
            }
        }
    }

    fun selectCard(card: Card) {
        _uiState.update { it.copy(selectedCard = card, errorMessage = null) }
    }

    fun updateStartingPrice(value: String) {
        // Solo dígitos y separador decimal (admite coma o punto).
        val filtered = value.filter { it.isDigit() || it == '.' || it == ',' }
        _uiState.update { it.copy(startingPrice = filtered, errorMessage = null) }
    }

    fun updateDescription(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun updateDuration(days: Int) {
        if (days in 1..7) {
            _uiState.update { it.copy(durationInDays = days) }
        }
    }

    /**
     * Actualiza la fecha de inicio. La hora se ajusta al siguiente bloque de 15 minutos.
     */
    fun updateStartDateTime(date: LocalDate, time: LocalTime) {
        val snapped = LocalDateTime.of(date, time).snapUpToQuarterHour()
        _uiState.update { it.copy(startDateTime = snapped, errorMessage = null) }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    /**
     * Valida el formulario y crea la subasta.
     * La fecha de inicio se envía en UTC.
     */
    fun createAuction() {
        val state = _uiState.value
        if (state.isCreating) return

        val card = state.selectedCard
        val price = state.startingPrice.replace(',', '.').toDoubleOrNull()
        val startInstant = state.startDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()

        if (card == null) {
            showError("Selecciona una carta.")
            return
        }
        if (price == null || price <= 0.0) {
            showError("Introduce un precio inicial válido.")
            return
        }
        if (startInstant.isBefore(Instant.now())) {
            showError("La fecha de inicio ya ha pasado. Elige otra.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, errorMessage = null) }

            try {
                val auction = createAuctionUseCase(
                    CreateAuction(
                        creatorId = TEMP_CREATOR_ID,
                        creatorUsername = TEMP_CREATOR_USERNAME,
                        cardId = card.id,
                        description = state.description.trim().ifBlank { null },
                        minPrize = price,
                        startedAt = startInstant.toString(),
                        durationInDays = state.durationInDays
                    )
                )

                _uiState.update { it.copy(isCreating = false, createdAuction = auction) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        errorMessage = "No se ha podido crear la subasta."
                    )
                }
            }
        }
    }

    /**
     * La pantalla ya ha reaccionado a la subasta creada.
     */
    fun onAuctionCreatedHandled() {
        _uiState.update { it.copy(createdAuction = null) }
    }
}