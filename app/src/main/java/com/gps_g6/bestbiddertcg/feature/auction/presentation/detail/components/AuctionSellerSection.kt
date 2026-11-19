package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.text.font.FontWeight
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.detail.AuctionDetailUiState
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.AvatarResources

@Composable
fun AuctionSellerSection(
    uiState: AuctionDetailUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.isOwner) return

    Column(
        modifier = modifier
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
        verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
    ) {
        Text(
            text = "Publicado por",
            style = AppTypography.bodyLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Background,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.sellerAvatar > 0) {
                    Image(
                        painter = painterResource(
                            id = AvatarResources.getDrawable(uiState.sellerAvatar)
                        ),
                        contentDescription = "Avatar del vendedor",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = uiState.sellerName.take(1).uppercase(),
                        style = AppTypography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }

            Text(
                text = uiState.sellerName,
                style = AppTypography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.padding(start = AppSpacing.m)
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right_text_secondary),
                contentDescription = "Ver perfil del vendedor",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Vista previa de la sección de vendedor de la subasta.
 */
@Preview(
    name = "Vendedor - Otro usuario",
    showBackground = true,
    backgroundColor = 0xFFFFF9F5
)
@Composable
private fun AuctionSellerSectionPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            AuctionSellerSection(
                uiState = AuctionDetailUiState(
                    isOwner = false,
                    sellerName = "Coleccionista123"
                )
            )
        }
    }
}
