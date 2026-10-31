package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.CollectionCardUiModel
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileSampleData
import com.gps_g6.bestbiddertcg.R

@Composable
fun ProfileCollection(
    cards: List<CollectionCardUiModel>,
    modifier: Modifier = Modifier
) {
    if (cards.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_empty_collection),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = AppSpacing.l,
                vertical = AppSpacing.m
            ),
            horizontalArrangement = Arrangement.spacedBy(
                AppSpacing.s
            ),
            verticalArrangement = Arrangement.spacedBy(
                AppSpacing.s
            )
        ) {
            items(
                items = cards,
                key = { it.id }
            ) {
                card ->
                CollectionCard(card = card)
            }
        }
    }
}

@Composable
private fun CollectionCard(
    card: CollectionCardUiModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f),
        shape = RoundedCornerShape(AppCornerRadius.small),
        color = PrimaryLight,
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.s),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.sample_piplup),
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.72f),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground =  true, heightDp = 600)
@Composable
private fun ProfileCollectionPreview() {
    BestBidderTCGTheme {
        ProfileCollection(
            cards = ProfileSampleData.profile.collection
        )
    }
}