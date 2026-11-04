package com.gps_g6.bestbiddertcg.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
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
    @DrawableRes navigationIcon: Int? = null,
    navigationDescription: String? = null,
    onNavigationClick: (() -> Unit)? = null,
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
                .heightIn(min = AppSpacing.xxl + AppSpacing.xl)
                .padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // navigation icon opcional
            if (navigationIcon != null) {
                IconButton(
                    onClick = { onNavigationClick?.invoke() },
                    enabled = onNavigationClick != null
                ) {
                    Icon(
                        painter = painterResource(navigationIcon),
                        contentDescription = navigationDescription,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(AppSpacing.xs))
            } else {
                Spacer(modifier = Modifier.width(AppSpacing.s))
            }

            // Screen title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            // acción opcional
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
        AppTopBar(title = "Iniciar sesión")
    }
}