
package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailStatus
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailUiState
import java.util.Locale

@Composable
fun AuctionInfoSection(
    uiState: AuctionDetailUiState,
    modifier: Modifier = Modifier
) {
    val isActive = uiState.status == AuctionDetailStatus.ACTIVE

    val infoItems = if (isActive) {
        listOf(
            "Estado de conservación" to uiState.cardCondition,
            "Precio de salida" to String.format(
                Locale("es", "ES"),
                "%.2f €",
                uiState.startingPrice
            ),
            "Incremento mínimo" to String.format(
                Locale("es", "ES"),
                "%.2f €",
                uiState.minimumIncrement
            ),
            "Publicada" to uiState.publishedAt
        )
    } else {
        listOf(
            "Estado de conservación" to uiState.cardCondition,
            "Incremento mínimo" to String.format(
                Locale("es", "ES"),
                "%.2f €",
                uiState.minimumIncrement
            ),
            "Inicio programado" to uiState.scheduledStart,
            "Duración" to uiState.duration
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
    ) {
        // Información de la subasta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .padding(horizontal = AppSpacing.l)
        ) {
            infoItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.m),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.first,
                        style = AppTypography.bodySmall,
                        color = TextSecondary
                    )

                    Text(
                        text = item.second,
                        style = AppTypography.bodyMedium,
                        color = TextPrimary
                    )
                }

                if (index < infoItems.lastIndex) {
                    HorizontalDivider(color = Border)
                }
            }
        }

        // Descripcion de la subasta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Text(
                text = "Descripción",
                style = AppTypography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = uiState.description,
                style = AppTypography.bodyLarge,
                color = TextPrimary
            )
        }
    }
}

/**
 * Vista previa de la sección de información de la subasta activa.
 */
@Preview(
    name = "Información - Activa",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveAuctionInfoPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            AuctionInfoSection(
                uiState = AuctionDetailUiState(
                    status = AuctionDetailStatus.ACTIVE,
                    cardCondition = "Buen estado",
                    startingPrice = 10.0,
                    minimumIncrement = 1.0,
                    publishedAt = "12/10/2026, 14h20",
                    description = "Subasto la carta ya que la tengo repe :p"
                )
            )
        }
    }
}

/**
 * Vista previa de la sección de información de la subasta proxima.
 */
@Preview(
    name = "Información - Próxima",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun UpcomingAuctionInfoPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            AuctionInfoSection(
                uiState = AuctionDetailUiState(
                    status = AuctionDetailStatus.UPCOMING,
                    cardCondition = "Buen estado",
                    minimumIncrement = 1.0,
                    scheduledStart = "18/11/2026, 18h00",
                    duration = "3 días",
                    description = "Subasto la carta ya que la tengo repe :p"
                )
            )
        }
    }
}
