
package com.gps_g6.bestbiddertcg.core.network

import android.content.Context
import android.util.Log
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.gps_g6.bestbiddertcg.BuildConfig
import com.gps_g6.bestbiddertcg.core.auth.EncryptedTokenStorage
import com.gps_g6.bestbiddertcg.core.auth.SessionManager
import com.gps_g6.bestbiddertcg.feature.auth.data.remote.AuthApi
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

    // Independent client: no bearer interceptor or authenticator.
    private val refreshClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val refreshApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(AuthApi::class.java)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .authenticator(
                TokenAuthenticator(
                    sessionManager = sessionManager,
                    refreshApi = refreshApi
                )
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

    private var cachedImageLoader: ImageLoader? = null

    @Synchronized
    fun imageLoader(context: Context): ImageLoader {
        return cachedImageLoader
            ?: createImageLoader(context).also {
                cachedImageLoader = it
            }
    }

    fun createImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context.applicationContext)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient }
                    )
                )
            }
            .build()
    }

    fun resolveImageUrl(image: String?): String? {
        if (image.isNullOrBlank()) return null

        if (!image.startsWith("/uploads/")) return null

        // Only allow paths served by our backend
        if (image.contains("..") ||
            image.contains("?") ||
            image.contains("#") ||
            image.contains("\\")
        ) return null

        return BASE_URL.trimEnd('/') + image
    }

}
