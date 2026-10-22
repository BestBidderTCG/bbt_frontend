package com.gps_g6.bestbiddertcg.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gps_g6.bestbiddertcg.feature.home.presentation.HomeScreen
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileScreen

/**
 * Coordinates the main screens and horizontal navigation.
 */
@Composable
fun BestBidderApp(){
    val pagerState = rememberPagerState(
        initialPage = AppPage.HOME.ordinal,
        pageCount = { AppPage.entries.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (AppPage.entries[page]) {
            AppPage.HOME -> HomeScreen()
            AppPage.PROFILE -> ProfileScreen()
        }
    }
}