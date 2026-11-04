package com.gps_g6.bestbiddertcg.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.45f))

        // logo placeholder
        Surface(
            modifier = Modifier.size(96.dp),
            shape = RoundedCornerShape(AppCornerRadius.large),
            color = PrimaryLight
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "BB",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Primary
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = Primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Text(
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            softWrap = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.m)
                .height(48.dp),
            shape = RoundedCornerShape(AppCornerRadius.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Text(
                text = stringResource(R.string.auth_login),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.m))

        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.m)
                .height(48.dp),
            shape = RoundedCornerShape(AppCornerRadius.medium),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Primary
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Primary
            )
        ) {
            Text(
                text = stringResource(R.string.auth_create_account),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun WelcomeScreenPreview() {
    BestBidderTCGTheme {
        WelcomeScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
