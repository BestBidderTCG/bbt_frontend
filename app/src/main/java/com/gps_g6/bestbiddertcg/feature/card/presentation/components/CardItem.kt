
package com.gps_g6.bestbiddertcg.feature.card.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary


// Representa visualmente una carta mediante su imagen.
@Composable
fun CardItem(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.small),
        color = AppSurface,
        border = BorderStroke(1.dp, Border)
    ) {
        // Placeholder de la imagen de la carta.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .background(
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.small)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Imagen no disponible",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


// Vista previa de una carta para comprobar su diseño.
@Preview(showBackground = true)
@Composable
private fun CardItemPreview() {
    BestBidderTCGTheme {
        CardItem(
            modifier = Modifier.padding(AppSpacing.s)
        )
    }
}