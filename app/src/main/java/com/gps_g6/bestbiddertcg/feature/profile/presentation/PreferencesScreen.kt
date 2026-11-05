package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.model.CardCategory
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.AppStepProgress
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.PreferenceItem

@Composable
fun PreferencesScreen(
    selectedPreferences: Set<CardCategory>,
    onPreferenceToggle: (CardCategory) -> Unit,
    onFinishClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        // Fixed top bar
        AppTopBar(
            title = stringResource(R.string.preferences_title),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(R.string.common_back),
            onNavigationClick = onBackClick
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.xl)
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Step indicator
            AppStepProgress(
                currentStep = 2,
                totalSteps = 2
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Heading
            Text(
                text = stringResource(R.string.preferences_heading),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = stringResource(R.string.preferences_description),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Selectable categories
            Column(
                verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
            ) {
                CardCategory.entries.forEach { category ->
                    PreferenceItem(
                        title = stringResource(category.displayNameRes),
                        selected = category in selectedPreferences,
                        onClick = {
                            onPreferenceToggle(category)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.m))

            // Helper text
            Text(
                text = stringResource(R.string.preferences_required),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.m)
                )
            }

            // Finish button
            Button(
                onClick = onFinishClick,
                enabled = selectedPreferences.isNotEmpty() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.preferences_finish),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))
        }
    }
}

@Preview(
    name = "Preferences - Selected",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PreferencesScreenPreview() {
    BestBidderTCGTheme {
        PreferencesScreen(
            selectedPreferences = setOf(
                CardCategory.POKEMON,
                CardCategory.DRAGON_BALL
            ),
            onPreferenceToggle = {},
            onFinishClick = {},
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Preferences - Empty",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PreferencesEmptyPreview() {
    BestBidderTCGTheme {
        PreferencesScreen(
            selectedPreferences = emptySet(),
            onPreferenceToggle = {},
            onFinishClick = {},
            onBackClick = {}
        )
    }
}
