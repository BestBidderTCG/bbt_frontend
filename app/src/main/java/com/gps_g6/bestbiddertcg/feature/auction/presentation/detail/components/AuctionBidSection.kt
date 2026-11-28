
package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components.BidLeadingSection
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailUiState
import java.util.Locale

@Composable
fun AuctionBidSection(
    uiState: AuctionDetailUiState,
    onBidClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = uiState.status == AuctionStatus.ACTIVE

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                )
                .padding(AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Text(
                    text = if (isActive) "Puja actual" else "Precio de salida",
                    color = TextSecondary
                )

                Text(
                    text = String.format(
                        Locale.forLanguageTag("es-ES"),
                        "%.2f €",
                        if (isActive) uiState.currentBid else uiState.startingPrice
                    ),
                    color = Primary,
                    style = AppTypography.headlineLarge
                )

                if (isActive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bid),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Unspecified
                        )

                        Text(
                            text = "${uiState.bidCount} pujas",
                            color = TextSecondary
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
            ) {
                Text(
                    text = if (isActive) "Tiempo restante" else "Comienza en",
                    color = TextSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock_active),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = uiState.remainingTime,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (isActive) {
            when {
                uiState.isOwner -> {
                    Button(
                        onClick = onHistoryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.size(AppSpacing.s))

                        Text(
                            text = "Ver historial de pujas",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                uiState.isWinningBid -> {
                    BidLeadingSection()
                }

                else -> {
                    Button(
                        onClick = onBidClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bid_white),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.size(AppSpacing.s))

                        Text(
                            text = "Pujar",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryLight,
                        shape = RoundedCornerShape(AppCornerRadius.medium)
                    )
                    .padding(AppSpacing.l),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Primary
                    )

                    Text(
                        text = "Subasta aún no iniciada",
                        color = Primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (uiState.isOwner) {
                    Text(
                        text = "Puedes editar esta subasta antes de que comience.",
                        color = TextPrimary
                    )
                }
            }
        }
    }
}


@Composable
private fun AuctionBidSectionPreviewContent(
    status: AuctionStatus,
    isOwner: Boolean,
    isWinningBid: Boolean = false
) {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            AuctionBidSection(
                uiState = AuctionDetailUiState(
                    status = status,
                    isOwner = isOwner,
                    isWinningBid = isWinningBid,
                    currentBid = 45.0,
                    startingPrice = 10.0,
                    bidCount = 12,
                    remainingTime = "2 h 35 min"
                ),
                onBidClick = {},
                onHistoryClick = {}
            )
        }
    }
}

/**
 * Vista previa de detalles de subasta activa de otro usuario.
 */
@Preview(
    name = "Activa - Otro usuario",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveOtherUserBidPreview() {
    AuctionBidSectionPreviewContent(
        status = AuctionStatus.ACTIVE,
        isOwner = false
    )
}

/**
 * Vista previa de detalles de subasta proxima de otro usuario.
 */
@Preview(
    name = "Próxima - Otro usuario",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun UpcomingOtherUserBidPreview() {
    AuctionBidSectionPreviewContent(
        status = AuctionStatus.UPCOMING,
        isOwner = false
    )
}

/**
 * Vista previa de detalles de subasta activa de mi subasta.
 */
@Preview(
    name = "Activa - Mi subasta",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveOwnerBidPreview() {
    AuctionBidSectionPreviewContent(
        status = AuctionStatus.ACTIVE,
        isOwner = true
    )
}

/**
 * Vista previa de detalles de subasta proxima de mi subasta.
 */
@Preview(
    name = "Próxima - Mi subasta",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun UpcomingOwnerBidPreview() {
    AuctionBidSectionPreviewContent(
        status = AuctionStatus.UPCOMING,
        isOwner = true
    )
}

/**
 * Vista previa de subasta activa en la que el usuario tiene la puja más alta.
 */
@Preview(
    name = "Activa - Voy ganando",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveWinningBidPreview() {
    AuctionBidSectionPreviewContent(
        status = AuctionStatus.ACTIVE,
        isOwner = false,
        isWinningBid = true
    )
}

