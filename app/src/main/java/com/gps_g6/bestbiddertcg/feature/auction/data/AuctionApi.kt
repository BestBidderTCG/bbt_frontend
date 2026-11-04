package com.gps_g6.bestbiddertcg.feature.auction.data

import retrofit2.http.GET

interface AuctionApi {

    // Obtiene las subastas activas y próximas disponibles.
    @GET("api/auction/active")
    suspend fun getActiveAuctions(): List<AuctionDto>
}
