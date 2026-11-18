package com.gps_g6.bestbiddertcg.core.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.gps_g6.bestbiddertcg.R

@Composable
fun MainTopBar(
    title: String,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppTopBar(
        title = title,
        modifier = modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
        ),
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_bell),
                    contentDescription = stringResource(
                        R.string.notifications_description
                    ),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(
                        R.string.settings_description
                    ),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}