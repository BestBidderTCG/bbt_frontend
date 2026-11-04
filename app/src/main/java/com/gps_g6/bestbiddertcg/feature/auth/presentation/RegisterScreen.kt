package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.auth.presentation.components.AuthPasswordField

@Composable
fun RegisterScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onContinueClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Fixed top bar
        AppTopBar(
            title = stringResource(R.string.auth_create_account),
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
            Spacer(
                modifier = Modifier.height(
                    AppSpacing.xxl + AppSpacing.xxl + AppSpacing.l
                )
            )

            // Main heading
            Text(
                text = stringResource(R.string.register_greeting),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = stringResource(R.string.register_description),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Email
            Text(
                text = stringResource(R.string.auth_email_label),
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.auth_email_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(AppCornerRadius.medium),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Border,
                    cursorColor = Primary,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.l))

            // Password
            AuthPasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.auth_password_label),
                placeholder = stringResource(R.string.register_password_hint)
            )

            Spacer(modifier = Modifier.height(AppSpacing.l))

            // Confirm password
            AuthPasswordField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.register_confirm_password),
                placeholder = stringResource(R.string.register_confirm_hint)
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Continue
            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(AppCornerRadius.medium),
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

            Spacer(modifier = Modifier.height(AppSpacing.m))

            // Login link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.auth_existing_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.width(AppSpacing.xs))

                TextButton(
                    onClick = onLoginClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.auth_login_link),
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))
        }
    }
}

@Preview(
    name = "Register Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RegisterScreenPreview() {
    BestBidderTCGTheme {
        RegisterScreen(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            onContinueClick = {},
            onLoginClick = {},
            onBackClick = {}
        )
    }
}
