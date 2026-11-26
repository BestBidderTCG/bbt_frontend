package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.core.ui.components.AppTopBar
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MyCardDetailsScreen(
    uiState: MyCardDetailsUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onAuctionClick: (String) -> Unit,
    onEditCardClick: () -> Unit,
    onCreateAuctionClick: () -> Unit,
    onDeleteCardClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        AppTopBar(
            title = stringResource(R.string.card_details_title),
            navigationIcon = R.drawable.ic_arrow_back,
            navigationDescription = stringResource(R.string.common_back),
            onNavigationClick = onBackClick
        )

        when (uiState) {
            MyCardDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            MyCardDetailsUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(AppSpacing.l),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.card_details_load_error),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(AppSpacing.m))
                    TextButton(onClick = onRetryClick) {
                        Text(stringResource(R.string.common_retry))
                    }
                }
            }

            is MyCardDetailsUiState.Success -> {
                val card = uiState.card
                val isAvailable =
                    card.availability is CardAvailability.Available

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(AppSpacing.l),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
                ) {
                    CardDetailsImage(card)

                    CardDetailsHeader(
                        card = card,
                        onDeleteCardClick = onDeleteCardClick
                    )

                    val auction =
                        card.availability as? CardAvailability.InAuction

                    if (auction != null) {
                        AuctionUnavailableNotice(
                            auction = auction,
                            onAuctionClick = onAuctionClick
                        )
                    }

                    CardDetailsInformation(card)

                    if (!card.description.isNullOrBlank()) {
                        CardDetailsDescription(card.description)
                    }
                }

                CardDetailsActions(
                    enabled = isAvailable,
                    onEditCardClick = onEditCardClick,
                    onCreateAuctionClick = onCreateAuctionClick
                )
            }
        }
    }
}

@Composable
private fun CardDetailsImage(card: MyCardDetailsUiModel) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.s),
        contentAlignment = Alignment.Center
    ) {
        if (isPreview) {
            Image(
                painter = painterResource(R.drawable.sample_piplup),
                contentDescription = card.name,
                modifier = Modifier
                    .width(190.dp)
                    .height(255.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            AsyncImage(
                model = card.imageUrl,
                imageLoader = NetworkModule.imageLoader(context),
                contentDescription = card.name,
                modifier = Modifier
                    .width(190.dp)
                    .height(255.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.sample_piplup),
                error = painterResource(R.drawable.sample_piplup),
                fallback = painterResource(R.drawable.sample_piplup)
            )
        }
    }
}

@Composable
private fun CardDetailsHeader(
    card: MyCardDetailsUiModel,
    onDeleteCardClick: () -> Unit
) {
    val auction =
        card.availability as? CardAvailability.InAuction

    Column(
        verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = card.name,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (auction == null) {
                IconButton(onClick = onDeleteCardClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(
                            R.string.card_details_delete
                        ),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (auction == null) {
            Text(
                text = stringResource(R.string.card_details_own_collection),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        } else {
            Surface(
                shape = RoundedCornerShape(50),
                color = PrimaryLight
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = AppSpacing.m,
                        vertical = AppSpacing.s
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        AppSpacing.s
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = when (auction.status) {
                            AuctionStatus.ACTIVE ->
                                stringResource(R.string.card_details_auction_active)
                            AuctionStatus.UPCOMING ->
                                stringResource(R.string.card_details_auction_scheduled)
                            AuctionStatus.FINISHED ->
                                stringResource(R.string.auction_status_finished)
                        },
                        color = Primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun AuctionUnavailableNotice(
    auction: CardAvailability.InAuction,
    onAuctionClick: (String) -> Unit
) {
    val isActive = auction.status == AuctionStatus.ACTIVE

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PrimaryLight,
        shape = RoundedCornerShape(AppCornerRadius.large)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AppSpacing.l,
                vertical = AppSpacing.m
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(
                    AppSpacing.m
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        AppSpacing.xs
                    )
                ) {
                    Text(
                        text = stringResource(
                            R.string.card_details_unavailable
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Text(
                        text = stringResource(
                            if (isActive) {
                                R.string.card_details_active_explanation
                            } else {
                                R.string.card_details_scheduled_explanation
                            }
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }

            TextButton(
                onClick = {
                    onAuctionClick(auction.auctionId)
                },
                contentPadding = PaddingValues(
                    horizontal = 0.dp,
                    vertical = 0.dp),
                modifier = Modifier.height(24.dp)
            ) {
                Text(
                    text = stringResource(
                        if (isActive) {
                            R.string.card_details_view_active_auction
                        } else {
                            R.string.card_details_view_scheduled_auction
                        }
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )

                Spacer(
                    modifier = Modifier.width(AppSpacing.s)
                )

                Icon(
                    painter = painterResource(
                        R.drawable.ic_arrow_forward
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun CardDetailsInformation(card: MyCardDetailsUiModel) {
    Surface(
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = AppSurface,
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(horizontal = AppSpacing.l)) {
            CardDetailRow(
                label = stringResource(R.string.category_string),
                value = card.category?.let { stringResource(it.labelRes) } ?: "—"
            )

            HorizontalDivider(color = Border)

            CardDetailRow(
                label = stringResource(R.string.collection_string),
                value = card.collection?.let { stringResource(it.labelRes) } ?: "—"
            )
            HorizontalDivider(color = Border)

            CardDetailRow(
                stringResource(R.string.card_details_number),
                card.number?.toString() ?: "—"
            )
            HorizontalDivider(color = Border)

            CardDetailRow(
                stringResource(R.string.card_details_condition),
                stringResource(card.condition.labelRes)
            )

            if (card.publishedAt != null) {
                HorizontalDivider(color = Border)
                CardDetailRow(
                    stringResource(R.string.card_details_published),
                    formatCardDate(card.publishedAt)
                )
            }
        }
    }
}

@Composable
private fun CardDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.m),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(AppSpacing.s))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun CardDetailsDescription(description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppSurface,
        shape = RoundedCornerShape(AppCornerRadius.medium),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Text(
                text = stringResource(R.string.description_string),
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun CardDetailsActions(
    enabled: Boolean,
    onEditCardClick: () -> Unit,
    onCreateAuctionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(AppSpacing.l)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
    ) {
        OutlinedButton(
            onClick = onEditCardClick,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(
                AppCornerRadius.medium
            ),
            contentPadding = PaddingValues(
                horizontal = AppSpacing.s
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Primary,
                disabledContainerColor = Border,
                disabledContentColor = TextSecondary
            ),
            border = if (enabled) {
                BorderStroke(1.dp, Primary)
            } else {
                BorderStroke(1.dp, Border)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                tint = if (enabled) Primary else TextSecondary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.width(AppSpacing.s)
            )

            Text(
                text = stringResource(R.string.card_details_edit),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }

        Button(
            onClick = onCreateAuctionClick,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(
                AppCornerRadius.medium
            ),
            contentPadding = PaddingValues(
                horizontal = AppSpacing.s
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = AppSurface,
                disabledContainerColor = Border,
                disabledContentColor = TextSecondary
            )
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.ic_auction_inactive
                ),
                contentDescription = null,
                tint = if (enabled) AppSurface else TextSecondary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.width(AppSpacing.s)
            )

            Text(
                text = stringResource(
                    R.string.create_auction_title
                ),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

private fun formatCardDate(value: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm")

    return runCatching {
        OffsetDateTime.parse(value)
            .atZoneSameInstant(ZoneId.systemDefault())
            .format(formatter)
    }.recoverCatching {
        LocalDateTime.parse(value).format(formatter)
    }.getOrDefault(value)
}

@Preview(name = "Disponible", showBackground = true, showSystemUi = true)
@Composable
private fun AvailableCardPreview() {
    BestBidderTCGTheme {
        MyCardDetailsScreen(
            uiState = MyCardDetailsUiState.Success(
                MyCardDetailsSampleData.available
            ),
            onBackClick = {},
            onRetryClick = {},
            onAuctionClick = {},
            onEditCardClick = {},
            onCreateAuctionClick = {},
            onDeleteCardClick = {}
        )
    }
}

@Preview(name = "Subasta activa", showBackground = true, showSystemUi = true)
@Composable
private fun ActiveAuctionCardPreview() {
    BestBidderTCGTheme {
        MyCardDetailsScreen(
            uiState = MyCardDetailsUiState.Success(
                MyCardDetailsSampleData.activeAuction
            ),
            onBackClick = {},
            onRetryClick = {},
            onAuctionClick = {},
            onEditCardClick = {},
            onCreateAuctionClick = {},
            onDeleteCardClick = {}
        )
    }
}

@Preview(name = "Subasta programada", showBackground = true, showSystemUi = true)
@Composable
private fun ScheduledAuctionCardPreview() {
    BestBidderTCGTheme {
        MyCardDetailsScreen(
            uiState = MyCardDetailsUiState.Success(
                MyCardDetailsSampleData.scheduledAuction
            ),
            onBackClick = {},
            onRetryClick = {},
            onAuctionClick = {},
            onEditCardClick = {},
            onCreateAuctionClick = {},
            onDeleteCardClick = {}
        )
    }
}
