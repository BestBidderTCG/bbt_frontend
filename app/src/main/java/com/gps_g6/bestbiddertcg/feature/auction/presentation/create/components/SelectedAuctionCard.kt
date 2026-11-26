package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary

@Composable
fun SelectedAuctionCard(
    card: CardSummary,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            AppCornerRadius.medium
        ),
        color = PrimaryLight,
        border = BorderStroke(
            width = 1.dp,
            color = Primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                AppSpacing.m
            )
        ) {
            AsyncImage(
                model = NetworkModule.resolveImageUrl(card.image),
                imageLoader = NetworkModule.imageLoader(context),
                contentDescription = card.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(54.dp)
                    .height(70.dp)
                    .clip(
                        RoundedCornerShape(
                            AppCornerRadius.small
                        )
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    AppSpacing.xs
                )
            ) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = card.category?.let {
                        stringResource(it.labelRes)
                    } ?: stringResource(
                        R.string.card_no_category
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}
