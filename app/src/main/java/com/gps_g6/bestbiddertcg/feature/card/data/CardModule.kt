package com.gps_g6.bestbiddertcg.feature.card.data

import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.card.data.remote.CardApi
import com.gps_g6.bestbiddertcg.feature.card.domain.repository.CardRepository
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetCardDetailsUseCase
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

    val getCardDetailsUseCase: GetCardDetailsUseCase by lazy {
        GetCardDetailsUseCase(cardRepository)
    }
}