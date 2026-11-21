
package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPickerBottomSheet(
    cards: List<CardSummary>,
    selectedCardId: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onConfirm: (CardSummary) -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var searchQuery by remember {
        mutableStateOf("")
    }

    var pendingCardId by remember(selectedCardId) {
        mutableStateOf(selectedCardId)
    }

    val filteredCards = remember(cards, searchQuery) {
        cards.filter { card ->
            card.name.contains(
                searchQuery.trim(),
                ignoreCase = true
            )
        }
    }

    val pendingCard = cards.firstOrNull {
        it.id == pendingCardId && !it.inAuction
    }

    val sheetHeight =
        LocalConfiguration.current.screenHeightDp.dp * 0.78f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppSurface,
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .padding(horizontal = AppSpacing.l)
        ) {
            Text(
                text = stringResource(
                    R.string.auction_card_picker_title
                ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(
                modifier = Modifier.height(AppSpacing.l)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        stringResource(
                            R.string.auction_card_picker_search_hint
                        )
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(
                            R.drawable.ic_search
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                )
            )

            Spacer(
                modifier = Modifier.height(AppSpacing.m)
            )

            Text(
                text = stringResource(
                    R.string.auction_card_picker_available
                ),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(AppSpacing.s)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary
                        )
                    }
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage,
                            color = TextSecondary
                        )

                        TextButton(onClick = onRetry) {
                            Text(
                                text = stringResource(
                                    R.string.common_retry
                                )
                            )
                        }
                    }
                }

                filteredCards.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                if (cards.isEmpty()) {
                                    R.string.auction_card_picker_empty
                                } else {
                                    R.string.auction_card_picker_no_results
                                }
                            ),
                            color = TextSecondary
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(
                            AppSpacing.s
                        ),
                        contentPadding = PaddingValues(
                            vertical = AppSpacing.s
                        )
                    ) {
                        items(
                            items = filteredCards,
                            key = { it.id }
                        ) { card ->
                            CardPickerItem(
                                card = card,
                                selected = card.id == pendingCardId,
                                onClick = {
                                    pendingCardId = card.id
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Border)

            Spacer(
                modifier = Modifier.height(AppSpacing.l)
            )

            Button(
                onClick = {
                    pendingCard?.let(onConfirm)
                },
                enabled = pendingCard != null &&
                        !isLoading &&
                        errorMessage == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = AppSurface
                )
            ) {
                Text(
                    text = stringResource(
                        R.string.auction_card_picker_title
                    ),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(AppSpacing.l)
            )
        }
    }
}

@Composable
private fun CardPickerItem(
    card: CardSummary,
    selected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val isAvailable = !card.inAuction

    val selectionDescription = stringResource(
        if (selected) {
            R.string.auction_card_picker_selected_description
        } else {
            R.string.auction_card_picker_unselected_description
        },
        card.name
    )

    Surface(
        onClick = onClick,
        enabled = isAvailable,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = selectionDescription
            },
        shape = RoundedCornerShape(
            AppCornerRadius.medium
        ),
        color = if (selected) PrimaryLight else AppSurface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary else Border
        )
    ) {
        Row(
            modifier = Modifier.padding(AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                AppSpacing.m
            )
        ) {
            AsyncImage(
                model = NetworkModule.resolveImageUrl(
                    card.image
                ),
                imageLoader = NetworkModule.imageLoader(context),
                contentDescription = card.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(54.dp)
                    .height(70.dp)
                    .clip(
                        RoundedCornerShape(
                            AppCornerRadius.small
                        )
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    AppSpacing.xs
                )
            ) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = card.category?.let {
                        stringResource(it.labelRes)
                    } ?: stringResource(
                        R.string.card_no_category
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Checkbox(
                checked = selected,
                onCheckedChange = { onClick() },
                enabled = isAvailable,
                modifier = Modifier,
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    uncheckedColor = TextSecondary
                )
            )
        }
    }
}
