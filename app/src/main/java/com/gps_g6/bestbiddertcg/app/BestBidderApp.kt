
package com.gps_g6.bestbiddertcg.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.gps_g6.bestbiddertcg.core.ui.components.BottomNavigationBar
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreScreen
import com.gps_g6.bestbiddertcg.feature.favorites.presentation.FavoritesScreen
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileScreen
import com.gps_g6.bestbiddertcg.feature.social.presentation.SocialScreen
import kotlinx.coroutines.launch

/**
 * Coordinates the main screens and horizontal navigation.
 */
@Composable
fun BestBidderApp() {
    val pagerState = rememberPagerState(
        initialPage = AppPage.EXPLORE.ordinal,
        pageCount = { AppPage.entries.size }
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedPage = AppPage.entries[pagerState.currentPage],
                onPageSelected = { page ->
                    scope.launch {
                        pagerState.animateScrollToPage(page.ordinal)
                    }
                },
                onAddClick = {
                    // TODO: Add auction creation action
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (AppPage.entries[page]) {
                AppPage.EXPLORE -> ExploreScreen()
                AppPage.SOCIAL -> SocialScreen()
                AppPage.FAVORITES -> FavoritesScreen()
                AppPage.PROFILE -> ProfileScreen()
            }
        }
    }
}
