
package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Success
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailUiState
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection

/**
 * Barra de encabezado de la pantalla de detalle de subasta.
 */
@Composable
fun AuctionCardHeader(
    uiState: AuctionDetailUiState,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.l),
        verticalAlignment = Alignment.Top
    ) {
        val context = LocalContext.current

        AsyncImage(
            model = uiState.cardImage,
            imageLoader = NetworkModule.imageLoader(context),
            contentDescription = uiState.cardName,
            modifier = Modifier
                .width(136.dp)
                .height(184.dp)
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                ),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.sample_piplup),
            error = painterResource(R.drawable.sample_piplup),
            fallback = painterResource(R.drawable.sample_piplup)
        )

        Spacer(modifier = Modifier.width(AppSpacing.l))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val isActive = uiState.status == AuctionStatus.ACTIVE

                Text(
                    text = when (uiState.status) {
                        AuctionStatus.ACTIVE -> stringResource(R.string.auction_status_active)
                        AuctionStatus.UPCOMING -> stringResource(R.string.auction_status_upcoming)
                        AuctionStatus.FINISHED -> stringResource(R.string.auction_status_finished)
                    },
                    modifier = Modifier
                        .background(
                            color = if (isActive) {
                                Success.copy(alpha = 0.12f)
                            } else {
                                PrimaryLight
                            },
                            shape = RoundedCornerShape(AppCornerRadius.large)
                        )
                        .padding(
                            horizontal = AppSpacing.m,
                            vertical = AppSpacing.xs
                        ),
                    color = if (isActive) Success else Primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                when {
                    !uiState.isOwner -> {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    role = Role.Button,
                                    onClick = onFavoriteClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorites_active),
                                contentDescription = stringResource(R.string.favorites_add),
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    uiState.status == AuctionStatus.UPCOMING -> {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    role = Role.Button,
                                    onClick = onEditClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = stringResource(R.string.auction_edit),
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = uiState.cardName,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.category_string),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Text(
                text = uiState.cardCategory?.let {
                    stringResource(it.labelRes)
                } ?: "—",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Text(
                text = stringResource(R.string.collection_string),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Text(
                text = uiState.cardCollection?.let {
                    stringResource(it.labelRes)
                } ?: "—",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Text(
                text = "N.º ${uiState.cardNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}


@Composable
private fun AuctionCardHeaderPreviewContent(
    status: AuctionStatus,
    isOwner: Boolean,
    isFavorite: Boolean = false
) {
    BestBidderTCGTheme {
        Box(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            AuctionCardHeader(
                uiState = AuctionDetailUiState(
                    cardName = "Piplup",
                    cardImage = "",
                    cardCategory = CardCategory.POKEMON,
                    cardCollection = CardCollection.POKEMON_151,
                    cardNumber = "393",
                    status = status,
                    isOwner = isOwner,
                    isFavorite = isFavorite
                ),
                onFavoriteClick = {},
                onEditClick = {}
            )
        }
    }
}

/**
 * Vista previa de la barra de encabezado de la pantalla de detalle de subasta activa de otro usuario.
 */
@Preview(
    name = "Activa - Otro usuario",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveOtherUserPreview() {
    AuctionCardHeaderPreviewContent(
        status = AuctionStatus.ACTIVE,
        isOwner = false
    )
}

/**
 * Vista previa de la barra de encabezado de la pantalla de detalle de subasta proxima de otro usuario.
 */
@Preview(
    name = "Proxima - Otro usuario",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun UpcomingOtherUserPreview() {
    AuctionCardHeaderPreviewContent(
        status = AuctionStatus.UPCOMING,
        isOwner = false
    )
}

/**
 * Vista previa de la barra de encabezado de la pantalla de detalle de subasta activa propia.
 */
@Preview(
    name = "Activa - Mi subasta",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun ActiveOwnerPreview() {
    AuctionCardHeaderPreviewContent(
        status = AuctionStatus.ACTIVE,
        isOwner = true
    )
}

/**
 * Vista previa de la barra de encabezado de la pantalla de detalle de subasta proxima propia.
 */
@Preview(
    name = "Proxima - Mi subasta",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun UpcomingOwnerPreview() {
    AuctionCardHeaderPreviewContent(
        status = AuctionStatus.UPCOMING,
        isOwner = true
    )
}
