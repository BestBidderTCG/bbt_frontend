package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun ProfileWalletCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = Surface,
        border = BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier.padding(AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        PrimaryLight,
                        RoundedCornerShape(AppCornerRadius.small)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_wallet),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.profile_wallet_title),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.xs)
                )

                Text(
                    text = stringResource(
                        R.string.profile_wallet_sample_balance
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_text_secondary),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileWalletCardPreview() {
    BestBidderTCGTheme {
        Box(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            ProfileWalletCard()
        }
    }
}
