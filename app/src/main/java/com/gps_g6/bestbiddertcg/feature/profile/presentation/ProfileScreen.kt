package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.*
import com.gps_g6.bestbiddertcg.R
import java.nio.file.WatchEvent

/**
 * Profile screen of BestBidderTCG.
 *
 */
@Composable
fun ProfileScreen(
    profile: ProfileUiState = ProfileSampleData.profile,
    onEditProfile: () -> Unit = {},
    initialTab: ProfileTab = ProfileTab.COLLECTION
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(initialTab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Top bar fija
        AppTopBar(
            title = stringResource(R.string.profile_title),
            actionIcon = R.drawable.ic_notifications,
            actionDescription = stringResource(
                R.string.notifications_description
            )
        )

        // Profile card fija
        ProfileHeader(
            profile = profile,
            onEditClick = onEditProfile,
            modifier = Modifier.padding(
                horizontal = AppSpacing.l,
                vertical = AppSpacing.l
            )
        )

        // Tabs fijas
        ProfileTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(
                horizontal = AppSpacing.l
            )
        )

        // Scrollable tab content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                ProfileTab.COLLECTION -> {
                    ProfileCollection(
                        cards = profile.collection
                    )
                }

                ProfileTab.AUCTIONS -> {
                    ProfileAuctions(
                        auctions = profile.auctions
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Profile - Collection",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProfileCollectionsScreenPreview() {
    BestBidderTCGTheme {
        ProfileScreen()
    }
}

@Preview(
    name = "Profile - Auctions",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProfileAuctionsScreenPreview() {
    BestBidderTCGTheme {
        ProfileScreen(
            initialTab = ProfileTab.AUCTIONS
        )
    }
}