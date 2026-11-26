package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid

/**
 * Estado de la pantalla de realización de pujas.
 */
data class PlaceBidUiState(
    val auctionId: String = "",

    // Información de la carta
    val cardName: String = "",
    val cardImage: String? = null,
    val cardCategory: String = "",

    // Información de la subasta
    val currentBid: Double = 0.0,
    val minimumIncrement: Double = 1.0,
    val remainingTime: String = "",

    // Datos del formulario
    val bidAmount: String = "",

    // Estados de la interfaz
    val hasOutbid: Boolean = false,
    val isAuctionFinished: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /**
     * Calcula el importe mínimo permitido.
     */
    val minimumAllowedBid: Double
        get() = currentBid + minimumIncrement

    /**
     * Convierte el importe introducido a Double.
     */
    val parsedBidAmount: Double?
        get() = bidAmount.replace(',', '.').toDoubleOrNull()

    /**
     * Comprueba si el importe introducido es válido.
     */
    val isBidAmountValid: Boolean
        get() = parsedBidAmount?.let { it >= minimumAllowedBid } ?: false
}