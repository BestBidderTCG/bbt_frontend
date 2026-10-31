package com.gps_g6.bestbiddertcg.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes actionIcon: Int? = null,
    actionDescription: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppSpacing.xl,
                    vertical = AppSpacing.s
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (actionIcon != null) {
                if (onActionClick != null) {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            painter = painterResource(actionIcon),
                            contentDescription = actionDescription,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(actionIcon),
                        contentDescription = actionDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(AppSpacing.m)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTopBarPreview() {
    BestBidderTCGTheme {
        AppTopBar(
            title = "Mi perfil"
        )
    }
}