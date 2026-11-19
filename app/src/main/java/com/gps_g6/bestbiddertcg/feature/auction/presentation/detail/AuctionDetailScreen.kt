package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components.AuctionBidSection
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components.AuctionCardHeader
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components.AuctionInfoSection
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components.AuctionSellerSection

@Composable
fun AuctionDetailScreen(
    uiState: AuctionDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit,
    onBidClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppSurface
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSpacing.xxl + AppSpacing.xl),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = "Detalle de subasta",
                    style = AppTypography.headlineMedium,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = TextPrimary
                    )
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppSpacing.l),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        style = AppTypography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }

            else -> {
                // Scrollable auction content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
                ) {
                    AuctionCardHeader(
                        uiState = uiState,
                        onFavoriteClick = onFavoriteClick,
                        onEditClick = onEditClick
                    )

                    AuctionBidSection(
                        uiState = uiState,
                        onBidClick = onBidClick,
                        onHistoryClick = onHistoryClick,
                        modifier = Modifier.padding(horizontal = AppSpacing.l)
                    )

                    AuctionInfoSection(
                        uiState = uiState,
                        modifier = Modifier.padding(horizontal = AppSpacing.l)
                    )

                    if (!uiState.isOwner) {
                        AuctionSellerSection(
                            uiState = uiState,
                            modifier = Modifier.padding(horizontal = AppSpacing.l)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.s))
                }
            }
        }
    }
}

/**
 * Vista previa de la pantalla de detalle de subasta.
 */
@Preview(
    name = "Detalle - Activa de otro usuario",
    showBackground = true,
    showSystemUi = false
)
@Composable
private fun AuctionDetailScreenPreview() {
    BestBidderTCGTheme {
        AuctionDetailScreen(
            uiState = AuctionDetailUiState(
                cardName = "Piplup",
                cardCategory = "Pokemon",
                cardCollection = "Pokemon 151",
                cardNumber = "393",
                cardImage = "",
                status = AuctionDetailStatus.ACTIVE,
                isOwner = false,
                currentBid = 45.0,
                startingPrice = 10.0,
                bidCount = 12,
                remainingTime = "2 h 35 min",
                cardCondition = "Buen estado",
                minimumIncrement = 1.0,
                publishedAt = "12/10/2026, 14h20",
                description = "Subasto la carta ya que la tengo repe :p",
                sellerName = "PokeCollector"
            ),
            onBackClick = {},
            onFavoriteClick = {},
            onEditClick = {},
            onBidClick = {},
            onHistoryClick = {}
        )
    }
}
