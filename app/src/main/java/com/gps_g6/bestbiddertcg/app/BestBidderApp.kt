
package com.gps_g6.bestbiddertcg.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreViewModel
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreViewModelFactory
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.CreateAuctionScreen
import com.gps_g6.bestbiddertcg.feature.auction.data.AuctionModule
import com.gps_g6.bestbiddertcg.core.ui.components.BottomNavigationBar
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreScreen
import com.gps_g6.bestbiddertcg.feature.favorites.presentation.FavoritesScreen
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileRoute
import com.gps_g6.bestbiddertcg.feature.social.presentation.SocialScreen
import kotlinx.coroutines.launch

/**
 * Página principal de la aplicación.
 */
@Composable
fun BestBidderApp() {
    val pagerState = rememberPagerState(
        initialPage = AppPage.EXPLORE.ordinal,
        pageCount = { AppPage.entries.size }
    )
    val scope = rememberCoroutineScope()
    var isCreatingAuction by remember { mutableStateOf(false) }

    // Gestiona el estado de la pantalla Explore.
    val exploreViewModel: ExploreViewModel = viewModel(
        factory = ExploreViewModelFactory(AuctionModule.auctionRepository)
    )

    Scaffold(
        bottomBar = {
            if (!isCreatingAuction) {
                BottomNavigationBar(
                    selectedPage = AppPage.entries[pagerState.currentPage],
                    onPageSelected = { page ->
                        scope.launch {
                            pagerState.animateScrollToPage(page.ordinal)
                        }
                    },
                    onAddClick = {
                        isCreatingAuction = true
                    }
                )
            }
        }
    ) { innerPadding ->
        if (isCreatingAuction) {
            CreateAuctionScreen(
                onBackClick = {
                    isCreatingAuction = false
                },
                onAuctionCreated = {
                    isCreatingAuction = false
                    // TODO: cuando haya sse quitamos
                    exploreViewModel.loadAuctions()
                }
            )
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { page ->
                when (AppPage.entries[page]) {
                    AppPage.EXPLORE -> ExploreScreen(
                        auctionRepository = AuctionModule.auctionRepository
                    )
                    AppPage.SOCIAL -> SocialScreen()
                    AppPage.FAVORITES -> FavoritesScreen()
                    AppPage.PROFILE -> ProfileRoute()
                }
            }
        }
    }
}
