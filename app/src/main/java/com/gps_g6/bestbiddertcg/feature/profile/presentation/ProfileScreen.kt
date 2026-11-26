package com.gps_g6.bestbiddertcg.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.components.*
import com.gps_g6.bestbiddertcg.R

/**
 * Profile screen of BestBidderTCG.
 *
 */
@Composable
fun ProfileScreen(
    profile: ProfileUiState = ProfileSampleData.profile,
    onEditProfile: () -> Unit = {},
    initialTab: ProfileTab = ProfileTab.COLLECTION,
    onRetryClick: () -> Unit = {},
    onCardClick: (CollectionCardUiModel) -> Unit = {},
    onAuctionClick: (AuctionUiModel) -> Unit = {},
    onRetryCardsClick: () -> Unit = {}
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(initialTab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Profile header: loading, error or data
        when {
            profile.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            profile.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.l),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profile.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier = Modifier.height(AppSpacing.m)
                    )

                    Button(onClick = onRetryClick) {
                        Text(stringResource(R.string.common_retry))
                    }
                }
            }

            else -> {
                ProfileHeader(
                    profile = profile,
                    onEditClick = onEditProfile,
                    modifier = Modifier.padding(
                        horizontal = AppSpacing.l,
                        vertical = AppSpacing.l
                    )
                )
            }
        }

        ProfileWalletCard(
            modifier = Modifier.padding(
                horizontal = AppSpacing.l,
                vertical = AppSpacing.m
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
                    when {
                        profile.isLoadingCards -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Primary)
                            }
                        }

                        profile.cardsError != null -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.profile_collection_load_error
                                    ),
                                    color = TextSecondary
                                )

                                TextButton(onClick = onRetryCardsClick) {
                                    Text(stringResource(R.string.common_retry))
                                }
                            }
                        }

                        else -> {
                            ProfileCollection(
                                cards = profile.collection,
                                onCardClick = onCardClick
                            )
                        }
                    }
                }

                ProfileTab.AUCTIONS -> {
                    ProfileAuctions(
                        auctions = profile.auctions,
                        onAuctionClick = onAuctionClick
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