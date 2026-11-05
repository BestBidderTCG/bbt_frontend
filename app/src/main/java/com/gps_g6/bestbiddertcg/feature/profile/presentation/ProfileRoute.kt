package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.feature.profile.di.ProfileModule

@Composable
fun ProfileRoute() {

    val factory = remember {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        ProfileViewModel::class.java
                    )
                )

                return ProfileViewModel(
                    profileManager = ProfileModule.profileManager
                ) as T
            }
        }
    }

    val viewModel: ProfileViewModel = viewModel(
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    ProfileScreen(
        profile = uiState,
        onRetryClick = {
            viewModel.loadProfile(forceRefresh = true)
        }
    )
}
