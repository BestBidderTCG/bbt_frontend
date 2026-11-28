
package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gps_g6.bestbiddertcg.core.ui.theme.Accent
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme

@Composable
fun BidConfirmationDialog(
    bidAmount: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 300.dp),
            shape = RoundedCornerShape(AppCornerRadius.large),
            color = AppSurface
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.l),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.m),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = PrimaryLight,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bid_orange),
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Text(
                    text = "¿Confirmar puja?",
                    style = AppTypography.headlineMedium,
                    color = TextPrimary
                )

                Text(
                    text = "Vas a realizar una puja por la siguiente cantidad:",
                    style = AppTypography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "%.2f €".format(bidAmount),
                    style = AppTypography.headlineLarge,
                    color = Primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = PrimaryLight,
                            shape = RoundedCornerShape(AppCornerRadius.small)
                        )
                        .padding(AppSpacing.m),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = Accent
                    )

                    Text(
                        text = "Esta puja es irrevocable. Una vez confirmada, no podrás retirarla.",
                        style = AppTypography.bodySmall,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Text(
                        text = "Confirmar puja",
                        style = AppTypography.labelMedium
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium)
                ) {
                    Text(
                        text = "Volver y revisar",
                        style = AppTypography.labelMedium,
                        color = Primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun BidConfirmationDialogPreview() {
    BestBidderTCGTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFF999999))
                .padding(16.dp)
        ) {
            BidConfirmationDialog(
                bidAmount = 46.00,
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}
