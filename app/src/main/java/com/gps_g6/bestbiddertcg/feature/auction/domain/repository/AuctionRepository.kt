package com.gps_g6.bestbiddertcg.feature.auction.domain.repository

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction

interface AuctionRepository {

    // Obtener todas las subastas sin filtros.
    suspend fun getAllAuctions(): List<Auction>

    // Obtener todas las subastas activas.
    suspend fun getActiveAuctions(): List<Auction>

    // Obtener todas las subastas con los filtros aplicados.
    suspend fun searchAuctions(
        query: String? = null,
        active: Boolean? = true,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        page: Int = 0,
        size: Int = 20
    ): List<Auction>

    // Crear una nueva subasta.
    suspend fun createAuction(request: CreateAuction): Auction
}
