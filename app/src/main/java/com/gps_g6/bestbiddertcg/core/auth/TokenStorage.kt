package com.gps_g6.bestbiddertcg.core.auth

interface TokenStorage {

    fun saveTokens(tokens: SessionTokens)

    fun getTokens(): SessionTokens?

    fun clearTokens()
}
