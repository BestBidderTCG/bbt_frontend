package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.profile.presentation.AuctionUiModel
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileSampleData
import com.gps_g6.bestbiddertcg.R

@Composable
fun AuctionCard(
    auction: AuctionUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(
                AppSpacing.m
            )
        ) {
            AuctionCardImage(auction.cardName)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    AppSpacing.s
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = auction.cardName,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    AuctionStatusBadge(auction.status)
                }

                Text(
                    text = when (auction.status) {
                        AuctionStatus.ACTIVE -> stringResource(R.string.auction_current_bid)
                        AuctionStatus.UPCOMING -> stringResource(R.string.auction_starting_price)
                        AuctionStatus.FINISHED -> stringResource(R.string.auction_winning_bid)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Text(
                    text = auction.price,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Primary
                )

                AuctionDetail(
                    detail = auction.detail,
                    status = auction.status
                )
            }
        }
    }
}

@Composable
private fun AuctionCardImage(
    name: String
) {
    Image(
        painter = painterResource(R.drawable.sample_piplup),
        contentDescription = name,
        modifier = Modifier
            .width(84.dp)
            .aspectRatio(0.72f)
            .clip(
                RoundedCornerShape(AppCornerRadius.small)
            ),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun AuctionStatusBadge(
    status: AuctionStatus
) {
    val label = when (status) {
        AuctionStatus.ACTIVE -> stringResource(R.string.auction_status_active)
        AuctionStatus.UPCOMING -> stringResource(R.string.auction_status_upcoming)
        AuctionStatus.FINISHED -> stringResource(R.string.auction_status_finished)
    }

    val color = when (status) {
        AuctionStatus.ACTIVE -> Success
        AuctionStatus.UPCOMING -> Primary
        AuctionStatus.FINISHED -> TextSecondary
    }

    Surface(
        shape = RoundedCornerShape(AppCornerRadius.small),
        color = color.copy(alpha = 0.10f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = AppSpacing.s,
                vertical = AppSpacing.xs
            ),
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
private fun AuctionDetail(
    detail: String,
    status: AuctionStatus
) {
    val iconRes = when (status) {
        AuctionStatus.ACTIVE -> R.drawable.ic_clock
        AuctionStatus.UPCOMING -> R.drawable.ic_calendar
        AuctionStatus.FINISHED -> R.drawable.ic_trophy
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            AppSpacing.xs
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )

        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuctionCardPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            ProfileSampleData.profile.auctions.forEach { auction ->
                AuctionCard(auction = auction)
            }
        }
    }
}