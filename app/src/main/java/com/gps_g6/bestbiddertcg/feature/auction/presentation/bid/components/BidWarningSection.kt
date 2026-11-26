package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme

@Composable
fun BidWarningSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFEDD5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = null,
            tint = Color(0xFFD0440A)
        )

        Text(
            text = "Las pujas son irrevocables. Una vez confirmada, no podrás retirarla.",
            color = Color(0xFF303846),
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 440)
@Composable
private fun BidWarningSectionPreview() {
    BestBidderTCGTheme {
        BidWarningSection()
    }
}