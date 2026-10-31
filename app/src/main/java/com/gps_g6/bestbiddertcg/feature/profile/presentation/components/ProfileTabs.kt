
package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileTab

@Composable
fun ProfileTabs(
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
    ) {
        ProfileTabItem(
            title = stringResource(R.string.profile_tab_collection),
            iconRes = if (selectedTab == ProfileTab.COLLECTION) {
                R.drawable.ic_collection_active
            } else {
                R.drawable.ic_collection_inactive
            },
            selected = selectedTab == ProfileTab.COLLECTION,
            onClick = { onTabSelected(ProfileTab.COLLECTION) },
            modifier = Modifier.weight(1f)
        )

        ProfileTabItem(
            title = stringResource(R.string.profile_tab_auctions),
            iconRes = if (selectedTab == ProfileTab.AUCTIONS) {
                R.drawable.ic_auction_active
            } else {
                R.drawable.ic_auction_inactive
            },
            selected = selectedTab == ProfileTab.AUCTIONS,
            onClick = { onTabSelected(ProfileTab.AUCTIONS) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileTabItem(
    title: String,
    @DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) Primary else TextSecondary

    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(52.dp)
                .padding(horizontal = AppSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                AppSpacing.s
            )
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    if (selected) Primary else TextSecondary
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileTabsPreview() {
    BestBidderTCGTheme {
        ProfileTabs(
            selectedTab = ProfileTab.AUCTIONS,
            onTabSelected = {},
            modifier = Modifier.padding(horizontal = AppSpacing.l)
        )
    }
}
