package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.AuctionUiModel
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileSampleData
import com.gps_g6.bestbiddertcg.R

@Composable
fun ProfileAuctions(
    auctions: List<AuctionUiModel>,
    modifier: Modifier = Modifier
) {
    if (auctions.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_empty_auctions),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(
                AppSpacing.m
            )
        ) {
            items(
                items = auctions,
                key = { it.id }
            ) { auction ->
                AuctionCard(auction = auction)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 650)
@Composable
private fun ProfileAuctionsPreview() {
    BestBidderTCGTheme {
        ProfileAuctions(
            auctions = ProfileSampleData.profile.auctions
        )
    }
}
