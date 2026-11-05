
package com.gps_g6.bestbiddertcg.feature.auction.data

import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase

// Proporciona las dependencias necesarias para la funcionalidad de subastas.
object AuctionModule {

    // Crea la API de subastas utilizando la instancia compartida de Retrofit.
    private val auctionApi: AuctionApi by lazy {
        NetworkModule.retrofit.create(AuctionApi::class.java)
    }

    // Crea el repositorio que utilizará la capa de dominio.
    val auctionRepository: AuctionRepository by lazy {
        AuctionRepositoryImpl(auctionApi)
    }

    // Crea el caso de uso para crear una nueva subasta.
    val createAuctionUseCase: CreateAuctionUseCase by lazy {
        CreateAuctionUseCase(auctionRepository)
    }
}
