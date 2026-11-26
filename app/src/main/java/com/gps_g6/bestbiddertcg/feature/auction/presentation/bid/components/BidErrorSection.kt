package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppTypography
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Error
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary

@Composable
fun BidErrorSection(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppSurface,
        shape = RoundedCornerShape(AppCornerRadius.small),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Error
        )
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.m),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_warning_red),
                    contentDescription = null,
                    tint = Error
                )

                Text(
                    text = "No se ha podido confirmar la puja",
                    color = Error,
                    style = AppTypography.labelMedium
                )
            }

            Text(
                text = "No hemos podido comprobar si se ha registrado. Consulta el historial antes de volver a intentarlo.",
                color = TextPrimary,
                style = AppTypography.bodySmall,
                modifier = Modifier.padding(start = AppSpacing.xl)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BidErrorSectionPreview() {
    BestBidderTCGTheme {
        BidErrorSection()
    }
}