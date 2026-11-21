package com.gps_g6.bestbiddertcg.feature.auction.domain.model

enum class AuctionStatus {
    UPCOMING,
    ACTIVE,
    FINISHED;

    companion object {
        fun fromBackend(value: String): AuctionStatus = when (value) {
            "UPCOMING" -> UPCOMING
            "IN_PROGRESS" -> ACTIVE
            "FINISHED" -> FINISHED
            else -> throw IllegalArgumentException(
                "Estado de subasta desconocido: $value"
            )
        }
    }
}
