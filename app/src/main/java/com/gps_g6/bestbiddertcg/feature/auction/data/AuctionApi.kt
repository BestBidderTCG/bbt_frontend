package com.gps_g6.bestbiddertcg.feature.auction.data

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface AuctionApi {

    // Obtener todas las subastas.
    @GET("api/auction/all")
    suspend fun getAllAuctions(
        @Query("sort") sort: String? = null
    ): List<AuctionSummaryDto>


    // Crear una nueva subasta.
    @POST("api/auction/create")
    suspend fun createAuction(
        @Body request: CreateAuctionRequestDto
    ): AuctionResponseDto

    // Obtener el detalle de una subasta.
    @GET("api/auction/{id}")
    suspend fun getAuctionById(
        @Path("id") auctionId: String
    ): AuctionDetailDto

}
