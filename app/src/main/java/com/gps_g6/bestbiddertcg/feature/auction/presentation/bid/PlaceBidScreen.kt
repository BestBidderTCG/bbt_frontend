package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components.*

@Composable
fun PlaceBidScreen(
    uiState: PlaceBidUiState,
    onBackClick: () -> Unit,
    onBidAmountChange: (String) -> Unit,
    onConfirmBid: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Barra superior
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
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
                    text = "Realizar puja",
                    style = AppTypography.headlineMedium,
                    color = TextPrimary
                )
            }
        }

        // Contenido desplazable
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
        ) {
            BidAuctionCard(
                cardName = uiState.cardName,
                cardCategory = uiState.cardCategory,
                cardImage = uiState.cardImage,
                remainingTime = uiState.remainingTime
            )

            BidAmountSection(
                currentBid = uiState.currentBid,
                minimumIncrement = uiState.minimumIncrement
            )

            BidInputSection(
                bidAmount = uiState.bidAmount,
                minimumAllowedBid = uiState.minimumAllowedBid,
                onBidAmountChange = onBidAmountChange
            )

            BidWarningSection()
        }

        // Botón inferior fijo
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppSurface,
            shadowElevation = 4.dp
        ) {
            Button(
                onClick = {
                    showConfirmationDialog = true
                },
                enabled = uiState.isBidAmountValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppSpacing.l,
                        vertical = AppSpacing.m
                    )
                    .height(60.dp),
                shape = RoundedCornerShape(AppCornerRadius.medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD0440A)
                )
            ) {
                Text(
                    text = "Continuar",
                    style = AppTypography.titleMedium
                )
            }
        }
    }

    // Diálogo de confirmación
    if (showConfirmationDialog) {
        BidConfirmationDialog(
            bidAmount = uiState.parsedBidAmount ?: 0.0,
            onConfirm = {
                showConfirmationDialog = false
                onConfirmBid()
            },
            onDismiss = {
                showConfirmationDialog = false
            }
        )
    }
}

@Preview(
    name = "Realizar puja",
    showBackground = true,
    showSystemUi = false,
    widthDp = 440,
    heightDp = 900
)
@Composable
private fun PlaceBidScreenPreview() {
    BestBidderTCGTheme {
        PlaceBidScreen(
            uiState = PlaceBidUiState(
                auctionId = "123",
                cardName = "Piplup",
                cardImage = null,
                cardCategory = "Pokémon",
                currentBid = 45.00,
                minimumIncrement = 1.00,
                remainingTime = "2 h 35 min",
                bidAmount = "46,00"
            ),
            onBackClick = {},
            onBidAmountChange = {},
            onConfirmBid = {}
        )
    }
}