package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun AppStepProgress(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    require(totalSteps > 0) {
        "totalSteps must be greater than zero"
    }
    require(currentStep in 1..totalSteps) {
        "currentStep must be between 1 and totalSteps"
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            repeat(totalSteps) { index ->
                val completed = index < currentStep

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(
                            RoundedCornerShape(
                                AppCornerRadius.small
                            )
                        )
                        .background(
                            if (completed) Primary else Border
                        )
                )
            }
        }

        Text(
            text = stringResource(
                R.string.step_progress,
                currentStep,
                totalSteps
            ),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppStepProgressPreview() {
    BestBidderTCGTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xxl)
        ) {
            AppStepProgress(
                currentStep = 1,
                totalSteps = 2
            )

            AppStepProgress(
                currentStep = 2,
                totalSteps = 2
            )
        }
    }
}
