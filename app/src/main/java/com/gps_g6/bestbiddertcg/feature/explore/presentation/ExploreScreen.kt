package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius

/**
 * Pantalla explore de la aplicacion.
 */
@Composable
fun ExploreScreen() {

    // Barra de busqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtro seleccionado de colecciones de cartas
    var selectedCardCollection by remember { mutableStateOf("Todas") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        ExploreHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.l)
        ) {
            SearchInput(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(AppSpacing.m))

            CardCollectionFilters(
                selectedCardCollection = selectedCardCollection,
                onCardCollectionSelected = { selectedCardCollection = it }
            )

            Spacer(modifier = Modifier.height(AppSpacing.l))

            SectionHeader(
                onFiltersClick = {
                    // TODO: Implementar filtros
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            EmptyState(
                onClearFilters = {
                    searchQuery = ""
                    selectedCardCollection = "Todas"
                }
            )
        }
    }
}

/**
 * Barra superior de la pantalla explore.
 */
@Composable
private fun ExploreHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Surface)
            .padding(horizontal = AppSpacing.xl),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Explorar",
            style = MaterialTheme.typography.headlineLarge
        )

        // Quitamos recuadro gris al hacer clic
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "Notificaciones",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) {
                        // TODO: Notificaciones
                    }
            )
        }
    }
}

/**
 * Barra de busqueda en la pantalla explore.
 */
@Composable
private fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppSpacing.m)
            .height(40.dp),
        shape = RoundedCornerShape(AppCornerRadius.small),
        color = Surface,
        border = BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AppSpacing.s),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Buscar",
                modifier = Modifier.size(20.dp)
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary
                ),
                // Texto cuando la barra de busqueda está vacia.
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Buscar cartas o subastas...",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    innerTextField()
                }
            )


        }
    }
}

/**
 * Filtros de colecciones de cartas en la pantalla explore.
 */
@Composable
private fun CardCollectionFilters(
    selectedCardCollection: String,
    onCardCollectionSelected: (String) -> Unit
) {
    val cardCollections = listOf("Todas", "Pokémon", "Yu-Gi-Oh!")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        cardCollections.forEach { cardCollection ->
            val isSelected = cardCollection == selectedCardCollection

            Surface(
                onClick = {
                    onCardCollectionSelected(cardCollection)
                },
                shape = RoundedCornerShape(50),
                color = if (isSelected) PrimaryLight else Surface,
                border = BorderStroke(
                    1.dp,
                    if (isSelected) PrimaryLight else Border
                )
            ) {
                Text(
                    text = cardCollection,
                    modifier = Modifier.padding(
                        horizontal = AppSpacing.m,
                        vertical = AppSpacing.s
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) Primary else TextPrimary
                )
            }
        }
    }
}

/**
 * Encabezado de seccion en la pantalla explore.
 */
@Composable
private fun SectionHeader(
    onFiltersClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Subastas activas",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    onFiltersClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_filters),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = "Filtros",
                style = MaterialTheme.typography.labelMedium,
                color = Primary
            )
        }
    }
}

/**
 * Estado vacio en la pantalla explore.
 */
@Composable
private fun EmptyState(
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_search_x),
            contentDescription = null,
            modifier = Modifier.size(75.dp)
        )

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Text(
            text = "No se han encontrado subastas",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Prueba a cambiar los filtros o realiza otra búsqueda.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Button(
            onClick = onClearFilters,
            shape = RoundedCornerShape(AppCornerRadius.medium),
            colors = buttonColors(
                containerColor = Primary
            )
        ) {
            Text(
                text = "Limpiar filtros",
                style = MaterialTheme.typography.labelMedium,
                color = Surface
            )
        }
    }

}


/**
 * Vista previa de la pantalla explore.
 */
@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    BestBidderTCGTheme {
        ExploreScreen()
    }
}