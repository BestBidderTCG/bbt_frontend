package com.gps_g6.bestbiddertcg.feature.explore.presentation

import androidx.compose.runtime.collectAsState
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.presentation.components.AuctionItem
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.Background
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.feature.explore.presentation.components.AuctionFilterOptions
import com.gps_g6.bestbiddertcg.feature.explore.presentation.components.AuctionFilterSheet
import com.gps_g6.bestbiddertcg.feature.explore.presentation.components.AuctionStatusFilter

/**
 * Pantalla explore de la aplicacion. Agregado el AuctionRepository para pruebas.
 */
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel
) {
    val auctions by viewModel.auctions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val sort by viewModel.sort.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var filterOptions by remember { mutableStateOf(AuctionFilterOptions()) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedCardCollection by remember { mutableStateOf("Todas") }
    var selectedCategory by remember { mutableStateOf("Todas") }

    ExploreContent(
        auctions = auctions,
        isLoading = isLoading,
        error = error,
        sort = sort,
        onSortSelected = { viewModel.changeSort(it) },
        searchQuery = searchQuery,
        onSearchQueryChange = { query ->
            searchQuery = query
            viewModel.applyFilters(
                query = query,
                active = when (filterOptions.status) {
                    AuctionStatusFilter.ACTIVE -> true
                    AuctionStatusFilter.UPCOMING -> false
                    null -> null
                },
                endingWithinDays = filterOptions.endingWithinDays,
                minPrice = filterOptions.minPrice.takeIf { it > 0.0 },
                maxPrice = filterOptions.maxPrice,
                condition = filterOptions.condition
                    .takeIf { it != com.gps_g6.bestbiddertcg.feature.explore.presentation.components.CardConditionFilter.ANY }
                    ?.name,
                collection = selectedCardCollection,
                category = selectedCategory
            )
        },
        onFiltersClick = {
            showFilterSheet = true
        },
        onClearFilters = {
            searchQuery = ""
            filterOptions = AuctionFilterOptions(endingWithinDays = null)
            selectedCardCollection = "Todas"
            selectedCategory = "Todas"
            viewModel.clearFilters()
        },
        selectedCardCollection = selectedCardCollection,
        onCardCollectionSelected = { collection ->
            selectedCardCollection = collection
            viewModel.applyFilters(
                query = searchQuery,
                active = when (filterOptions.status) {
                    AuctionStatusFilter.ACTIVE -> true
                    AuctionStatusFilter.UPCOMING -> false
                    null -> null
                },
                endingWithinDays = filterOptions.endingWithinDays,
                minPrice = filterOptions.minPrice.takeIf { it > 0.0 },
                maxPrice = filterOptions.maxPrice,
                condition = filterOptions.condition
                    .takeIf { it != com.gps_g6.bestbiddertcg.feature.explore.presentation.components.CardConditionFilter.ANY }
                    ?.name,
                collection = collection,
                category = selectedCategory
            )
        }
    )

    if (showFilterSheet) {
        AuctionFilterSheet(
            initialFilters = filterOptions,
            onDismiss = {
                showFilterSheet = false
            },
            onApply = { options ->
                filterOptions = options
                showFilterSheet = false
                viewModel.applyFilters(
                    query = searchQuery,
                    active = when (options.status) {
                        AuctionStatusFilter.ACTIVE -> true
                        AuctionStatusFilter.UPCOMING -> false
                        null -> null
                    },
                    endingWithinDays = options.endingWithinDays,
                    minPrice = options.minPrice.takeIf { it > 0.0 },
                    maxPrice = options.maxPrice,
                    condition = options.condition
                        .takeIf { it != com.gps_g6.bestbiddertcg.feature.explore.presentation.components.CardConditionFilter.ANY }
                        ?.name,
                    collection = selectedCardCollection,
                    category = selectedCategory
                )
            }
        )
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
    val cardCollections = listOf("Todas", "Pokemon", "Riftbound", "Magic")

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
    sort: AuctionSort,
    onSortSelected: (AuctionSort) -> Unit,
    onFiltersClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Subastas",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SortSelector(
                selected = sort,
                onSortSelected = onSortSelected
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
}

// Etiqueta de cada criterio de ordenación.
private fun AuctionSort.label(): String = when (this) {
    AuctionSort.POPULAR -> "Más populares"
    AuctionSort.ENDING_SOON -> "Terminan antes"
    AuctionSort.ENDING_LATE -> "Terminan después"
    AuctionSort.PRICE_ASC -> "Precio: menor a mayor"
    AuctionSort.PRICE_DESC -> "Precio: mayor a menor"
    AuctionSort.NAME_ASC -> "Nombre: A-Z"
    AuctionSort.NAME_DESC -> "Nombre: Z-A"
    AuctionSort.NUMBER_ASC -> "Número: ascendente"
    AuctionSort.NUMBER_DESC -> "Número: descendente"
    AuctionSort.RECENT_FIRST -> "Comienzo: Más recientes"
    AuctionSort.OLDEST_FIRST -> "Comienzo: Más antiguas"
}

// Grupos separados por una línea, como en el diseño.
private val sortGroups = listOf(
    listOf(AuctionSort.POPULAR),
    listOf(AuctionSort.ENDING_SOON, AuctionSort.ENDING_LATE),
    listOf(AuctionSort.PRICE_ASC, AuctionSort.PRICE_DESC),
    listOf(AuctionSort.NAME_ASC, AuctionSort.NAME_DESC),
    listOf(AuctionSort.NUMBER_ASC, AuctionSort.NUMBER_DESC),
    listOf(AuctionSort.RECENT_FIRST, AuctionSort.OLDEST_FIRST)
)
/**
 * Botón y menú "Ordenar por" de la pantalla explore.
 */
@Composable
private fun SortSelector(
    selected: AuctionSort,
    onSortSelected: (AuctionSort) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(AppCornerRadius.small),
            color = Surface,
            border = BorderStroke(1.dp, Border)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = AppSpacing.m,
                    vertical = AppSpacing.s
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = selected.label(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Surface)
        ) {
            Text(
                text = "ORDENAR POR",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(
                    horizontal = AppSpacing.l,
                    vertical = AppSpacing.s
                )
            )

            sortGroups.forEach { group ->
                // Línea separadora entre grupos.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border)
                )

                group.forEach { option ->
                    val isSelected = option == selected

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        },
                        leadingIcon = {
                            Text(
                                text = if (isSelected) "✓" else "",
                                color = Primary,
                                modifier = Modifier.width(16.dp)
                            )
                        },
                        onClick = {
                            expanded = false
                            onSortSelected(option)
                        },
                        modifier = Modifier.background(
                            if (isSelected) PrimaryLight else Surface
                        )
                    )
                }
            }
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
 * Contenido de la pantalla explore.
 */
@Composable
private fun ExploreContent(
    auctions: List<AuctionSummary>,
    isLoading: Boolean,
    error: String?,
    sort: AuctionSort,
    onSortSelected: (AuctionSort) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    onClearFilters: () -> Unit,
    selectedCardCollection: String,
    onCardCollectionSelected: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.l)
        ) {
            SearchInput(
                value = searchQuery,
                onValueChange = onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(AppSpacing.m))

            CardCollectionFilters(
                selectedCardCollection = selectedCardCollection,
                onCardCollectionSelected = onCardCollectionSelected
            )

            Spacer(modifier = Modifier.height(AppSpacing.l))

            SectionHeader(
                sort = sort,
                onSortSelected = onSortSelected,
                onFiltersClick = onFiltersClick
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            when {
                // Muestra el estado de carga
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Muestra el estado de error
                error != null -> {
                    Text(
                        text = error ?: "",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Muestra el estado vacio
                auctions.isEmpty() -> {
                    EmptyState(
                        onClearFilters = onClearFilters
                    )
                }

                // Muestra el estado de carga de subastas

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.m),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.m)
                    ) {
                        items(
                            items = auctions,
                            key = { auction -> auction.id }
                        ) { auction ->
                            AuctionItem(
                                cardName = auction.cardName,
                                cardImage = auction.cardImage,
                                currentBid = auction.currentBid,
                                remainingTime = formatRemainingDays(auction.remainingDays)
                            )
                        }
                    }
                }

            }
        }
    }
}

// Da formato a los días que faltan para que termine la subasta.
private fun formatRemainingDays(days: Int): String = when {
    days <= 0 -> "Finalizada"
    days == 1 -> "1 día"
    else -> "$days días"
}

/**
 * Vista previa de la pantalla explore. Usada para pruebas de diseño.
 */
@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    BestBidderTCGTheme {
        ExploreContent(
            auctions = emptyList(),
            isLoading = false,
            error = null,
            sort = AuctionSort.ENDING_SOON,
            onSortSelected = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onFiltersClick = {},
            onClearFilters = {},
            selectedCardCollection = "Todas",
            onCardCollectionSelected = {}
        )
    }
}