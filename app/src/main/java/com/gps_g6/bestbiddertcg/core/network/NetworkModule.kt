package com.gps_g6.bestbiddertcg.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkModule {

    // URL base del baackend al que se conectará la aplicación.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Configura la conversión de JSON a objetos Kotlin.
    // Ignora los campos de la respuesta que no estén definidos en los DTO.
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Configura el cliente HTTP encargado de gestionar las conexiones con el servidor.
    private val okHttpClient = OkHttpClient.Builder()
        .build()

    // Crea una única instancia de Retrofit cuando se utiliza por primera vez.
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
}
