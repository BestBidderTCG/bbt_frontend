package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.AvatarItem

private val availableAvatars = (1..9).toList()

@Composable
fun AvatarSelectionScreen(
    selectedAvatar: Int?,
    onAvatarSelected: (Int) -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        // Fixed top bar
        AppTopBar(
            title = stringResource(R.string.avatar_title),
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
            Spacer(modifier = Modifier.height(AppSpacing.xxl + AppSpacing.s))

            // Heading
            Text(
                text = stringResource(R.string.avatar_heading),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = stringResource(R.string.avatar_description),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Avatar grid: 3 columns
            Column(
                verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
            ) {
                availableAvatars.chunked(3).forEach { rowAvatars ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            AppSpacing.m
                        )
                    ) {
                        rowAvatars.forEach { avatarNum ->
                            AvatarItem(
                                avatarNum = avatarNum,
                                selected = selectedAvatar == avatarNum,
                                onClick = {
                                    onAvatarSelected(avatarNum)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Confirm button
            Button(
                onClick = onConfirmClick,
                enabled = selectedAvatar != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.avatar_confirm),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))
        }
    }
}

@Preview(
    name = "Avatar Selection",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AvatarSelectionScreenPreview() {
    BestBidderTCGTheme {
        AvatarSelectionScreen(
            selectedAvatar = 1,
            onAvatarSelected = {},
            onConfirmClick = {},
            onBackClick = {}
        )
    }
}