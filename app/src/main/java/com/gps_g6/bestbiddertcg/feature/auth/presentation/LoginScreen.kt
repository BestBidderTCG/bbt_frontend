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
fun LoginScreen(
    identifier: String,
    onIdentifierChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        // Fixed top bar
        AppTopBar(
            title = stringResource(R.string.auth_login),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(
                R.string.common_back
            ),
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

            // Greeting
            Text(
                text = stringResource(R.string.login_greeting),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            // Description
            Text(
                text = stringResource(R.string.login_description),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.widthIn(max = 280.dp)
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Identifier
            Text(
                text = stringResource(
                    R.string.auth_identifier_label
                ),
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.s))

            OutlinedTextField(
                value = identifier,
                onValueChange = onIdentifierChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.auth_identifier_hint
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
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

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Password
            AuthPasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(
                    R.string.auth_password_label
                ),
                placeholder = stringResource(
                    R.string.login_password_hint
                )
            )

            // Forgot password
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(
                    onClick = onForgotPasswordClick,
                    contentPadding = PaddingValues(
                        horizontal = 0.dp
                    )
                ) {
                    Text(
                        text = stringResource(
                            R.string.auth_forgot_password
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.m))

            // Login button
            Button(
                onClick = onLoginClick,
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
                    text = stringResource(R.string.auth_login),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.s))

            // Register link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.auth_no_account
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(
                    modifier = Modifier.width(AppSpacing.xs)
                )

                TextButton(
                    onClick = onRegisterClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.auth_register_link
                        ),
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
    name = "Login Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LoginScreenPreview() {
    BestBidderTCGTheme {
        LoginScreen(
            identifier = "",
            onIdentifierChange = {},
            password = "",
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onBackClick = {}
        )
    }
}
