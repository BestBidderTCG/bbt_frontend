package com.gps_g6.bestbiddertcg.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun DeleteAccountRequestedScreen(
    onUnderstoodClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(horizontal = AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))

        Icon(
            painter = painterResource(R.drawable.ic_check_circle),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(44.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        Text(
            text = stringResource(
                R.string.delete_account_requested_title
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = stringResource(
                R.string.delete_account_requested_description
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = stringResource(
                R.string.delete_account_requested_recovery
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Button(
            onClick = onUnderstoodClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(AppCornerRadius.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Surface
            )
        ) {
            Text(
                text = stringResource(R.string.common_understood),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeleteAccountRequestedScreenPreview() {
    BestBidderTCGTheme {
        DeleteAccountRequestedScreen(
            onUnderstoodClick = {}
        )
    }
}
