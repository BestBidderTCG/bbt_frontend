package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun PreferenceItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        color = if (selected) PrimaryLight else Surface,
        shape = RoundedCornerShape(AppCornerRadius.medium),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary else Border
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = null,
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    uncheckedColor = TextSecondary,
                    checkmarkColor = Surface
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) {
                        androidx.compose.ui.text.font.FontWeight.SemiBold
                    } else {
                        androidx.compose.ui.text.font.FontWeight.Normal
                    }
                ),
                color = if (selected) Primary else TextPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferenceItemPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            PreferenceItem(
                title = "Pokémon",
                selected = true,
                onClick = {}
            )

            PreferenceItem(
                title = "Magic: The Gathering",
                selected = false,
                onClick = {}
            )
        }
    }
}
