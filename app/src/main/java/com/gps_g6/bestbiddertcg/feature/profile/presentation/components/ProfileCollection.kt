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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.profile.presentation.CollectionCardUiModel
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileSampleData
import com.gps_g6.bestbiddertcg.R


@Composable
fun ProfileCollection(
    cards: List<CollectionCardUiModel>,
    modifier: Modifier = Modifier,
    onCardClick: (CollectionCardUiModel) -> Unit = {}
) {
    if (cards.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.profile_empty_collection
                ),
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
            ) { card ->
                CollectionCard(
                    card = card,
                    onClick = { onCardClick(card) }
                )
            }
        }
    }
}

@Composable
private fun CollectionCard(
    card: CollectionCardUiModel,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f),
        shape = RoundedCornerShape(AppCornerRadius.small),
        color = PrimaryLight,
        border = BorderStroke(1.dp, Border)
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.sample_piplup),
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            val context = LocalContext.current

            AsyncImage(
                model = card.imageUrl,
                imageLoader = NetworkModule.imageLoader(context),
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.sample_piplup),
                error = painterResource(R.drawable.sample_piplup),
                fallback = painterResource(R.drawable.sample_piplup)
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