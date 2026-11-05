package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.AppStepProgress

@Composable
fun CreateProfileScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    avatarNum: Int?,
    onAvatarClick: () -> Unit,
    onContinueClick: () -> Unit,
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
            title = stringResource(R.string.create_profile_title),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(R.string.common_back),
            onNavigationClick = onBackClick
        )

        // Scrollable form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.xl)
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            AppStepProgress(
                currentStep = 1,
                totalSteps = 2
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Heading
            Text(
                text = stringResource(R.string.create_profile_heading),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = stringResource(
                    R.string.create_profile_description
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.widthIn(max = 260.dp)
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Avatar selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAvatarClick),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_profile_active
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Primary
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Text(
                    text = stringResource(
                        R.string.create_profile_avatar
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Username
            Text(
                text = stringResource(
                    R.string.create_profile_username
                ),
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.create_profile_username_hint
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Border,
                    cursorColor = Primary,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Text(
                text = stringResource(
                    R.string.create_profile_username_help
                ),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.l))

            // Description
            Text(
                text = stringResource(R.string.create_profile_bio),
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.create_profile_bio_hint
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary
                ),
                minLines = 3,
                maxLines = 3,
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Border,
                    cursorColor = Primary,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Continue button
            Button(
                onClick = onContinueClick,
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
                    text = stringResource(R.string.auth_continue),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))
        }
    }
}

@Preview(
    name = "Create Profile - Step 1",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun CreateProfileScreenPreview() {
    BestBidderTCGTheme {
        CreateProfileScreen(
            username = "",
            onUsernameChange = {},
            description = "",
            onDescriptionChange = {},
            avatarNum = null,
            onAvatarClick = {},
            onContinueClick = {},
            onBackClick = {}
        )
    }
}
