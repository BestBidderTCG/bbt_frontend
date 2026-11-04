package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun DropdownField(
    label: String,
    placeholder: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppSpacing.m)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(AppCornerRadius.large))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.large)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )

            ChevronDown()
        }
    }
}

@Composable
fun SearchDropdownField(
    label: String,
    placeholder: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppSpacing.m)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(AppCornerRadius.large))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.large)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchSymbol()

            Spacer(modifier = Modifier.width(AppSpacing.m))

            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )

            ChevronDown()
        }
    }
}

@Composable
private fun SearchSymbol() {
    Canvas(
        modifier = Modifier.size(24.dp)
    ) {
        drawCircle(
            color = TextSecondary,
            radius = size.width * 0.30f,
            center = Offset(
                size.width * 0.42f,
                size.height * 0.42f
            ),
            style = Stroke(width = 2.dp.toPx())
        )

        drawLine(
            color = TextSecondary,
            start = Offset(
                size.width * 0.64f,
                size.height * 0.64f
            ),
            end = Offset(
                size.width * 0.90f,
                size.height * 0.90f
            ),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
private fun ChevronDown() {
    Canvas(
        modifier = Modifier.size(20.dp)
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.35f)
            lineTo(size.width * 0.5f, size.height * 0.65f)
            lineTo(size.width * 0.8f, size.height * 0.35f)
        }

        drawPath(
            path = path,
            color = TextSecondary,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}