package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun DurationSelector(
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = "Duración",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.padding(start = AppSpacing.m)
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            (1..7).forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(AppCornerRadius.small))
                        .background(
                            if (selectedDuration == day) {
                                PrimaryLight
                            } else {
                                Surface
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (selectedDuration == day) {
                                Primary
                            } else {
                                Border
                            },
                            shape = RoundedCornerShape(AppCornerRadius.small)
                        )
                        .clickable {
                            onDurationSelected(day)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (selectedDuration == day) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            }
                        ),
                        color = if (selectedDuration == day) {
                            Primary
                        } else {
                            TextSecondary
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Duración en días (1-7)",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(start = AppSpacing.m)
        )
    }
}

@Composable
fun EstimatedEndDate(
    dateText: String = "15/11/2026, 12h45"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppCornerRadius.medium))
            .background(PrimaryLight)
            .padding(AppSpacing.l)
    ) {
        Text(
            text = "Finalización estimada",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Spacer(modifier = Modifier.width(AppSpacing.m))

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextPrimary
            )
        }
    }
}