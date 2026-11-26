package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun BidInputSection(
    bidAmount: String,
    minimumAllowedBid: Double,
    onBidAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tu puja (€)",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = bidAmount,
                onValueChange = { value ->
                    if (value.matches(Regex("^\\d*([.,]\\d{0,2})?$"))) {
                        onBidAmountChange(value)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "%.2f".format(minimumAllowedBid)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Text(
                text = "Introduce un importe igual o superior a %.2f €."
                    .format(minimumAllowedBid),
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 440)
@Composable
private fun BidInputSectionPreview() {
    BestBidderTCGTheme {
        BidInputSection(
            bidAmount = "",
            minimumAllowedBid = 46.00,
            onBidAmountChange = {}
        )
    }
}