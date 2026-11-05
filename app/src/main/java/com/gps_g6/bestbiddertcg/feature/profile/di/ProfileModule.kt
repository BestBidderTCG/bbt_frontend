package com.gps_g6.bestbiddertcg.feature.profile.di

import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.profile.data.ProfileRepositoryImpl
import com.gps_g6.bestbiddertcg.feature.profile.data.remote.ProfileApi
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager

object ProfileModule {

    val profileManager: ProfileManager by lazy {

        val api = NetworkModule.retrofit
            .create(ProfileApi::class.java)

        val repository = ProfileRepositoryImpl(api)

        ProfileManager(
            repository = repository,
            sessionManager = NetworkModule.sessionManager
        )
    }
}
