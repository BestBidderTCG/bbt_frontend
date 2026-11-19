package com.gps_g6.bestbiddertcg.app

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailViewModel
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailViewModelFactory
import com.gps_g6.bestbiddertcg.feature.profile.di.ProfileModule
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailScreen
import com.gps_g6.bestbiddertcg.core.ui.components.MainTopBar
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreViewModel
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreViewModelFactory
import com.gps_g6.bestbiddertcg.feature.auction.data.AuctionModule
import com.gps_g6.bestbiddertcg.core.ui.components.BottomNavigationBar
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.CreateAuctionRoute
import com.gps_g6.bestbiddertcg.feature.explore.presentation.ExploreScreen
import com.gps_g6.bestbiddertcg.feature.favorites.presentation.FavoritesScreen
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileRoute
import com.gps_g6.bestbiddertcg.feature.social.presentation.SocialScreen
import kotlinx.coroutines.launch

/**
 * Página principal de la aplicación.
 */
@Composable
fun BestBidderApp(
    onCardClick: (String) -> Unit,
    onNotificationsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = AppPage.EXPLORE.ordinal,
        pageCount = { AppPage.entries.size }
    )
    val scope = rememberCoroutineScope()

    var isCreatingAuction by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedAuction by remember {
        mutableStateOf<AuctionSummary?>(null)
    }

    val currentPage = AppPage.entries[pagerState.currentPage]
    val topBarTitle = stringResource(currentPage.titleRes)

    // Gestiona el estado de la pantalla Explore.
    val exploreViewModel: ExploreViewModel = viewModel(
        factory = ExploreViewModelFactory(AuctionModule.auctionRepository)
    )

    Scaffold(
        topBar = {
            if (!isCreatingAuction && selectedAuction == null) {
                MainTopBar(
                    title = topBarTitle,
                    onNotificationsClick = onNotificationsClick,
                    onSettingsClick = onSettingsClick
                )
            }
        },
        bottomBar = {
            if (!isCreatingAuction && selectedAuction == null) {
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
        if (selectedAuction != null) {
            BackHandler {
                selectedAuction = null
            }

            val auction = selectedAuction!!

            val detailViewModel: AuctionDetailViewModel = viewModel(
                key = "auction_detail_${auction.id}",
                factory = AuctionDetailViewModelFactory(
                    getAuctionDetailUseCase = AuctionModule.getAuctionDetailUseCase,
                    profileManager = ProfileModule.profileManager
                )
            )

            val detailUiState by detailViewModel.uiState.collectAsState()

            LaunchedEffect(auction.id) {
                detailViewModel.loadAuction(auction.id)
            }

            AuctionDetailScreen(
                uiState = detailUiState,
                onBackClick = {
                    selectedAuction = null
                },
                onFavoriteClick = {},
                onEditClick = {},
                onBidClick = {},
                onHistoryClick = {},
                modifier = Modifier.padding(innerPadding)
            )
        } else if (isCreatingAuction) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CreateAuctionRoute(
                    onBackClick = {
                        isCreatingAuction = false
                    },
                    onAuctionCreated = {
                        isCreatingAuction = false
                        exploreViewModel.loadAuctions()
                    }
                )
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { page ->
                when (AppPage.entries[page]) {
                    AppPage.EXPLORE -> ExploreScreen(
                        viewModel = exploreViewModel,
                        onAuctionClick = { auction ->
                            selectedAuction = auction
                        }
                    )

                    AppPage.SOCIAL -> SocialScreen()
                    AppPage.FAVORITES -> FavoritesScreen()
                    AppPage.PROFILE -> ProfileRoute(onCardClick = onCardClick)
                }
            }
        }
    }
}