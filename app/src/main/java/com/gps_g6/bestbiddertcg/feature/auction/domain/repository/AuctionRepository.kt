package com.gps_g6.bestbiddertcg.feature.auction.domain.repository

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction

interface AuctionRepository {

    // Obtener todas las subastas sin filtros.
    suspend fun getAllAuctions(sort: AuctionSort = AuctionSort.ENDING_SOON): List<AuctionSummary>

    // Crear una nueva subasta.
    suspend fun createAuction(request: CreateAuction): Auction
}
