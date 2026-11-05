package com.gps_g6.bestbiddertcg.feature.card.data

import retrofit2.http.GET

interface CardApi {

    // Cartas del usuario autenticado.
    @GET("api/card/user/my-cards")
    suspend fun getMyCards(): List<CardDto>
}