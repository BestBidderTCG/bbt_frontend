package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase

object CardModule {

    private val cardApi: CardApi by lazy {
        NetworkModule.retrofit.create(CardApi::class.java)
    }

    val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardApi)
    }

    val getMyCardsUseCase: GetMyCardsUseCase by lazy {
        GetMyCardsUseCase(cardRepository)
    }
}