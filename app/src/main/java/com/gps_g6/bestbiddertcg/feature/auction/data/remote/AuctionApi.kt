package com.gps_g6.bestbiddertcg.feature.auction.data.remote

import com.gps_g6.bestbiddertcg.feature.auction.data.remote.dto.AuctionResponseDto
import com.gps_g6.bestbiddertcg.feature.auction.data.remote.dto.AuctionSummaryDto
import com.gps_g6.bestbiddertcg.feature.auction.data.remote.dto.CreateAuctionRequestDto
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

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
}
