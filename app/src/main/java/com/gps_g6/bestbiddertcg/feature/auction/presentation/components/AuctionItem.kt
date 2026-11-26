
package com.gps_g6.bestbiddertcg.feature.auction.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

// Representa una subasta con la información de su carta.
@Composable
fun AuctionItem(
    cardName: String,
    cardImage: String?,
    currentBid: Double,
    remainingTime: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = AppSurface,
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.s),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val context = LocalContext.current

            AsyncImage(
                model = cardImage,
                imageLoader = NetworkModule.imageLoader(context),
                contentDescription = cardName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.sample_piplup),
                error = painterResource(R.drawable.sample_piplup),
                fallback = painterResource(R.drawable.sample_piplup)
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            // Nombre de la carta.
            Text(
                text = cardName,
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            // Indica si se muestra el precio inicial o la puja actual.
            Text(
                text = "Puja actual",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            // Muestra el importe correspondiente.
            Text(
                text = String.format("%.2f €", currentBid),
                color = Primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            // Muestra el tiempo restante de la subasta.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = "Tiempo restante",
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = remainingTime,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
    }
}

// Vista previa de una subasta para comprobar su diseño.
@Preview(showBackground = true)
@Composable
private fun AuctionItemPreview() {
    BestBidderTCGTheme {
        AuctionItem(
            cardName = "Piplup",
            cardImage = "",
            currentBid = 0.67,
            remainingTime = "2 h 35 min",
            modifier = Modifier.padding(AppSpacing.s)
        )
    }
}
