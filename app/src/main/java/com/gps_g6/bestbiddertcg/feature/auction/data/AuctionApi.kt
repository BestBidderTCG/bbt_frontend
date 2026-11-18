package com.gps_g6.bestbiddertcg.feature.auction.data

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuctionApi {

    // Obtener todas las subastas con los filtros aplicados.
    @GET("api/auction/search")
    suspend fun searchAuctions(
        @Query("q") query: String? = null,
        @Query("active") active: Boolean? = true,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<AuctionResponseDto>

    // Obtener todas las subastas.
    @GET("api/auction/all")
    suspend fun getAllAuctions(): List<AuctionResponseDto>

    @GET("api/auction/active")
    suspend fun getActiveAuctions(): List<AuctionResponseDto>

    // Crear una nueva subasta.
    @POST("api/auction/create")
    suspend fun createAuction(
        @Body request: CreateAuctionRequestDto
    ): AuctionResponseDto
}
