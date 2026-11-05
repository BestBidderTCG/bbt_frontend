package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card

/**
 * Lista de cartas de la colección del usuario para elegir cuál subastar.
 */
@Composable
fun CardSelectionList(
    cards: List<Card>,
    selectedCardId: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onCardSelected: (Card) -> Unit,
    onRetry: () -> Unit
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.l),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        }

        errorMessage != null -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onRetry) {
                    Text(text = "Reintentar", color = Primary)
                }
            }
        }

        cards.isEmpty() -> {
            SelectionPlaceholder(
                icon = R.drawable.ic_grid,
                title = "Tu colección está vacía",
                description = "Añade cartas a tu colección para poder subastarlas"
            )
        }

        else -> {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.s)) {
                cards.forEach { card ->
                    CardOptionRow(
                        card = card,
                        selected = card.id == selectedCardId,
                        onClick = { onCardSelected(card) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardOptionRow(
    card: Card,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = if (selected) PrimaryLight else AppSurface,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) Primary else Border
        )
    ) {
        Column(modifier = Modifier.padding(AppSpacing.m)) {
            Text(
                text = card.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Text(
                text = listOfNotNull(
                    card.category,
                    card.collection,
                    card.num?.let { "#$it" },
                    cardStateLabel(card.state)
                ).joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

private fun cardStateLabel(state: String): String = when (state) {
    "NEW" -> "Nueva"
    "NEAR_MINT" -> "Casi nueva"
    "GOOD" -> "Buen estado"
    "LIGHT_PLAYED" -> "Marcas ligeras"
    "POOR" -> "Deteriorada"
    else -> state
}

/**
 * Diálogo para elegir una carta de la colección del usuario.
 */
@Composable
fun CardPickerDialog(
    cards: List<Card>,
    selectedCardId: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onCardSelected: (Card) -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Elige una carta") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CardSelectionList(
                    cards = cards,
                    selectedCardId = selectedCardId,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onCardSelected = onCardSelected,
                    onRetry = onRetry
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}