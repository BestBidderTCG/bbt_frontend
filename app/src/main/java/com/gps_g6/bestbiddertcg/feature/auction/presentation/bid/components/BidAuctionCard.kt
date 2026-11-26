
package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun BidAuctionCard(
    cardName: String,
    cardCategory: String,
    cardImage: String?,
    remainingTime: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppSurface,
        shape = RoundedCornerShape(AppCornerRadius.medium),
        border = BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.l)
        ) {
            AsyncImage(
                model = cardImage,
                contentDescription = cardName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 100.dp, height = 140.dp)
                    .clip(RoundedCornerShape(AppCornerRadius.small))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
            ) {
                Text(
                    text = cardName,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = cardCategory,
                    color = TextSecondary,
                    fontSize = 17.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock_active),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Finaliza en $remainingTime",
                        color = TextSecondary,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BidAuctionCardPreview() {
    BestBidderTCGTheme {
        BidAuctionCard(
            cardName = "Piplup",
            cardCategory = "Pokémon",
            cardImage = null,
            remainingTime = "2 h 35 min"
        )
    }
}
