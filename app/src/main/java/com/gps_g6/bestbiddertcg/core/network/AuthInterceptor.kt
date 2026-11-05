
package com.gps_g6.bestbiddertcg.core.network

import com.gps_g6.bestbiddertcg.core.auth.AccessTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: AccessTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val path = request.url.encodedPath

        if (
            path == "/api/auth/login" ||
            path == "/api/auth/register" ||
            path == "/api/auth/refresh"
        ) {
            return chain.proceed(request)
        }

        val token = tokenProvider.getAccessToken()

        if (token.isNullOrBlank()) {
            return chain.proceed(request)
        }

        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
