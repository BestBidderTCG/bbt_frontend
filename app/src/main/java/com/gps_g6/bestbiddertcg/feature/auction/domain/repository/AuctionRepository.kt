package com.gps_g6.bestbiddertcg.feature.auction.domain.repository

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction

interface AuctionRepository {

    // Obtiene las subastas activas y próximas.
    suspend fun getActiveAuctions(): List<Auction>

    // Crear una nueva subasta.
    suspend fun createAuction(request: CreateAuction): Auction
}
