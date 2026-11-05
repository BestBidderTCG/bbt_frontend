package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun SelectionPlaceholder(
    icon: Int,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(AppCornerRadius.medium)
            )
            .padding(AppSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.height(36.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SelectedCardPreview(
    cardName: String = "Piplup"
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = AppSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier.padding(AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sample_piplup),
                contentDescription = "Carta de $cardName",
                modifier = Modifier.size(width = 80.dp, height = 112.dp)
            )

            Spacer(modifier = Modifier.width(AppSpacing.l))

            Column {
                Text(
                    text = cardName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = "Carta seleccionada de Mi colección",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun SelectedCardPreviewPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l)
        ) {
            SelectedCardPreview(
                cardName = "Piplup"
            )
        }
    }
}