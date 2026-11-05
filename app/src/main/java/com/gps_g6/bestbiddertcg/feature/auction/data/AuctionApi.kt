package com.gps_g6.bestbiddertcg.feature.auction.data

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface AuctionApi {

    // Obtiene las subastas activas y próximas disponibles.
    @GET("api/auction/active")
    suspend fun getActiveAuctions(): List<AuctionResponseDto>

    // Crear una nueva subasta.
    @POST("api/auction/create")
    suspend fun createAuction(
        @Body request: CreateAuctionRequestDto
    ): AuctionResponseDto
}
