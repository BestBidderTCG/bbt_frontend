package com.gps_g6.bestbiddertcg.core.auth

interface AccessTokenProvider {

    fun getAccessToken(): String?
}
