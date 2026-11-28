package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary

@Composable
fun BidAmountSection(
    currentBid: Double,
    minimumIncrement: Double,
    modifier: Modifier = Modifier
) {
    val minimumAllowedBid = currentBid + minimumIncrement

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppSurface,
        shape = RoundedCornerShape(AppCornerRadius.large),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Puja actual",
                    color = TextSecondary,
                    style = AppTypography.bodyLarge
                )
                Text(
                    text = "%.2f €".format(currentBid),
                    color = TextPrimary,
                    style = AppTypography.bodyLarge
                )
            }

            HorizontalDivider(color = Border)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Incremento mínimo",
                    color = TextSecondary,
                    style = AppTypography.bodyLarge
                )
                Text(
                    text = "%.2f €".format(minimumIncrement),
                    color = TextPrimary,
                    style = AppTypography.bodyLarge
                )
            }

            HorizontalDivider(color = Border)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Puja mínima permitida",
                    color = TextPrimary,
                    style = AppTypography.labelMedium
                )

                Text(
                    text = "%.2f €".format(minimumAllowedBid),
                    color = Primary,
                    style = AppTypography.headlineLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 440)
@Composable
private fun BidAmountSectionPreview() {
    BestBidderTCGTheme {
        BidAmountSection(
            currentBid = 45.00,
            minimumIncrement = 1.00
        )
    }
}