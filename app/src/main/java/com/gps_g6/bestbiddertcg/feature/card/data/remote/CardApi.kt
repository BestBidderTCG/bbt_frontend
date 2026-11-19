package com.gps_g6.bestbiddertcg.feature.card.data.remote

import com.gps_g6.bestbiddertcg.feature.card.data.remote.dto.CardDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CardApi {

    @GET("api/card/user/my-cards")
    suspend fun getMyCards(): List<CardDto>

    @GET("api/card/{id}")
    suspend fun getCardDetails(
        @Path("id") cardId: String
    ): CardDto
}
