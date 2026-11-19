package com.gps_g6.bestbiddertcg.feature.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.components.ConfirmationDialog
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@StringRes
private fun DeleteAccountError.toStringRes(): Int = when (this) {
    DeleteAccountError.CHECK_FAILED ->
        R.string.delete_account_error_check

    DeleteAccountError.REQUEST_FAILED ->
        R.string.delete_account_error_request

    DeleteAccountError.REQUEST_UNCERTAIN ->
        R.string.delete_account_error_uncertain

    DeleteAccountError.CONFLICT ->
        R.string.delete_account_error_conflict

    DeleteAccountError.CONFLICT_CHECK_FAILED ->
        R.string.delete_account_error_conflict_check
}

@Composable
fun DeleteAccountScreen(
    uiState: DeleteAccountUiState,
    isSubmitting: Boolean,
    error: DeleteAccountError?,
    onBackClick: () -> Unit,
    onDeleteRequestClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    var showDeleteConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        AppTopBar(
            title = stringResource(R.string.settings_delete_account),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(R.string.common_back),
            onNavigationClick = {
                if (!isSubmitting) onBackClick()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppSpacing.xl,
                    vertical = AppSpacing.xl
                )
        ) {

            if (error != null) {
                Text(
                    text = stringResource(error.toStringRes()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Error
                )

                Spacer(modifier = Modifier.height(AppSpacing.l))
            }

            when (uiState) {
                DeleteAccountUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }

                DeleteAccountUiState.Allowed -> {
                    DeleteAccountAllowedContent(
                        isSubmitting = isSubmitting,
                        onDeleteRequestClick = {
                            showDeleteConfirmation = true
                        },
                        onCancelClick = onBackClick
                    )
                }

                is DeleteAccountUiState.Blocked -> {
                    DeleteAccountBlockedContent(
                        state = uiState,
                        onBackClick = onBackClick
                    )
                }

                DeleteAccountUiState.Error -> {
                    Button(
                        onClick = onRetryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.common_retry)
                        )
                    }
                }

            }

            if (isSubmitting) {
                Spacer(modifier = Modifier.height(AppSpacing.l))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
        }
    }

    if (
        uiState == DeleteAccountUiState.Allowed &&
        showDeleteConfirmation &&
        !isSubmitting
    ) {
        ConfirmationDialog(
            title = stringResource(R.string.delete_account_dialog_title),
            message = stringResource(R.string.delete_account_dialog_message),
            confirmText = stringResource(
                R.string.delete_account_confirm_button
            ),
            cancelText = stringResource(R.string.common_cancel),
            buttonColor = Error,
            onDismiss = {
                showDeleteConfirmation = false
            },
            onConfirm = {
                showDeleteConfirmation = false
                onDeleteRequestClick()
            }
        )
    }

}

@Composable
private fun DeleteAccountBlockedContent(
    state: DeleteAccountUiState.Blocked,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Error.copy(alpha = 0.10f),
                RoundedCornerShape(AppCornerRadius.medium)
            )
            .padding(AppSpacing.l)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_warning_red),
                contentDescription = null,
                tint = Error,
                modifier = Modifier.size(22.dp)
            )

            Text(
                text = stringResource(
                    R.string.delete_account_blocked_title
                ),
                style = MaterialTheme.typography.labelMedium,
                color = Error
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Text(
            text = stringResource(
                R.string.delete_account_blocked_description
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = Error
        )
    }

    Spacer(modifier = Modifier.height(AppSpacing.xl))

    DeleteAccountCard {
        if (state.hasActiveAuction) {
            DeleteAccountInfoRow(
                iconRes = R.drawable.ic_auction_active,
                title = stringResource(
                    R.string.delete_account_active_auction_title
                ),
                description = stringResource(
                    R.string.delete_account_active_auction_description
                )
            )
        }

        if (state.hasActiveAuction && state.isWinningAuction) {
            HorizontalDivider(
                color = Border,
                modifier = Modifier.padding(horizontal = AppSpacing.l)
            )
        }

        if (state.isWinningAuction) {
            DeleteAccountInfoRow(
                iconRes = R.drawable.ic_trophy,
                title = stringResource(
                    R.string.delete_account_winning_auction_title
                ),
                description = stringResource(
                    R.string.delete_account_winning_auction_description
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(56.dp))

    OutlinedButton(
        onClick = onBackClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        border = androidx.compose.foundation.BorderStroke(1.dp, Error),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Error
        )
    ) {
        Text(
            text = stringResource(R.string.delete_account_back_settings),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun DeleteAccountAllowedContent(
    isSubmitting: Boolean,
    onDeleteRequestClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Error.copy(alpha = 0.10f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                tint = Error,
                modifier = Modifier.size(40.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(AppSpacing.xxl))

    Text(
        text = stringResource(R.string.delete_account_confirm_title),
        style = MaterialTheme.typography.headlineMedium,
        color = TextPrimary
    )

    Spacer(modifier = Modifier.height(AppSpacing.m))

    Text(
        text = stringResource(
            R.string.delete_account_confirm_description
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
    )

    Spacer(modifier = Modifier.height(AppSpacing.xl))

    DeleteAccountCard {
        DeleteAccountInfoRow(
            iconRes = R.drawable.ic_calendar_orange,
            title = stringResource(
                R.string.delete_account_recovery_title
            ),
            description = stringResource(
                R.string.delete_account_recovery_description
            )
        )
    }

    Spacer(modifier = Modifier.height(AppSpacing.xl))

    Text(
        text = stringResource(
            R.string.delete_account_final_warning
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
    )

    Spacer(modifier = Modifier.height(AppSpacing.xl))

    Button(
        onClick = onDeleteRequestClick,
        enabled = !isSubmitting,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = Error,
            contentColor = Surface
        )
    ) {
        Text(
            text = stringResource(R.string.delete_account_request),
            style = MaterialTheme.typography.labelMedium
        )
    }

    Spacer(modifier = Modifier.height(AppSpacing.m))

    OutlinedButton(
        onClick = onCancelClick,
        enabled = !isSubmitting,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        border = androidx.compose.foundation.BorderStroke(1.dp, Error),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Error
        )
    ) {
        Text(
            text = stringResource(R.string.common_cancel),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun DeleteAccountCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(AppCornerRadius.medium)
            )
            .border(
                1.dp,
                Border,
                RoundedCornerShape(AppCornerRadius.medium)
            ),
        content = content
    )
}

@Composable
private fun DeleteAccountInfoRow(
    iconRes: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.m),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Preview(name = "Delete Account - Blocked", showBackground = true)
@Composable
private fun DeleteAccountBlockedPreview() {
    BestBidderTCGTheme {
        DeleteAccountScreen(
            uiState = DeleteAccountUiState.Blocked(
                hasActiveAuction = true,
                isWinningAuction = true
            ),
            isSubmitting = false,
            error = null,
            onRetryClick = {},
            onBackClick = {},
            onDeleteRequestClick = {}
        )
    }
}

@Preview(name = "Delete Account - Allowed", showBackground = true)
@Composable
private fun DeleteAccountAllowedPreview() {
    BestBidderTCGTheme {
        DeleteAccountScreen(
            uiState = DeleteAccountUiState.Allowed,
            isSubmitting = false,
            error = null,
            onRetryClick = {},
            onBackClick = {},
            onDeleteRequestClick = {}
        )
    }
}
