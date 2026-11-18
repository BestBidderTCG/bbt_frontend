package com.gps_g6.bestbiddertcg.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun SettingsScreen(
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onChangePasswordClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        AppTopBar(
            title = stringResource(R.string.settings_title),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(R.string.common_back),
            onNavigationClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppSpacing.l,
                    vertical = AppSpacing.xl
                )
        ) {

            SettingsSectionTitle(
                text = stringResource(R.string.settings_preferences)
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            SettingsContainer {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppSpacing.m,
                            vertical = AppSpacing.s
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bell),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(AppSpacing.m))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.notifications_description
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.xs))

                        Text(
                            text = stringResource(
                                R.string.settings_notifications_description
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = onNotificationsChange,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Primary,
                            checkedThumbColor = Surface,
                            uncheckedTrackColor = Border,
                            uncheckedThumbColor = Surface,
                            uncheckedBorderColor = Border
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            SettingsSectionTitle(
                text = stringResource(R.string.settings_security)
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            SettingsContainer {
                SettingsActionRow(
                    title = stringResource(
                        R.string.settings_change_password
                    ),
                    iconRes = R.drawable.ic_lock,
                    onClick = onChangePasswordClick
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            SettingsSectionTitle(
                text = stringResource(R.string.settings_legal)
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            SettingsContainer {
                SettingsActionRow(
                    title = stringResource(
                        R.string.settings_privacy_policy
                    ),
                    iconRes = R.drawable.ic_shield,
                    onClick = onPrivacyClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = AppSpacing.l
                    ),
                    color = Border
                )

                SettingsActionRow(
                    title = stringResource(R.string.settings_terms),
                    iconRes = R.drawable.ic_doc,
                    onClick = onTermsClick
                )
            }

            Spacer(modifier = Modifier.height(52.dp))

            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Surface
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(AppSpacing.s))

                Text(
                    text = stringResource(R.string.settings_logout),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.l))

            TextButton(
                onClick = onDeleteAccountClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_delete_account
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = Error
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))
        }
    }
}

@Composable
private fun SettingsSectionTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
    )
}

@Composable
private fun SettingsContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(AppCornerRadius.medium)
            )
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(AppCornerRadius.medium)
            ),
        content = content
    )
}

@Composable
private fun SettingsActionRow(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.xl),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(AppSpacing.m))

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(
                R.drawable.ic_chevron_right_text_secondary
            ),
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    var notificationsEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    BestBidderTCGTheme {
        SettingsScreen(
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = {
                notificationsEnabled = it
            },
            onChangePasswordClick = {},
            onPrivacyClick = {},
            onTermsClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onBackClick = {}
        )
    }
}
