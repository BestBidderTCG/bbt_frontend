
package com.gps_g6.bestbiddertcg.core.network

import android.content.Context
import android.util.Log
import com.gps_g6.bestbiddertcg.BuildConfig
import com.gps_g6.bestbiddertcg.core.auth.EncryptedTokenStorage
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    lateinit var sessionManager: SessionManager
        private set

    fun initialize(context: Context) {
        if (::sessionManager.isInitialized) return

        sessionManager = SessionManager(
            tokenStorage = EncryptedTokenStorage(
                context.applicationContext
            )
        )
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("BestBidderHTTP", message)
    }.apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(sessionManager)
            )
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }
}
