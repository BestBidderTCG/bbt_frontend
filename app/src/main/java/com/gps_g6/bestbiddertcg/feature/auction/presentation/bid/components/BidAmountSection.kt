package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

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
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Puja actual", color = TextSecondary, fontSize = 18.sp)
                Text(
                    text = "%.2f €".format(currentBid),
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            }

            HorizontalDivider(color = Border)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Incremento mínimo", color = TextSecondary, fontSize = 18.sp)
                Text(
                    text = "%.2f €".format(minimumIncrement),
                    color = TextPrimary,
                    fontSize = 18.sp
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
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "%.2f €".format(minimumAllowedBid),
                    color = Color(0xFFD0440A),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
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