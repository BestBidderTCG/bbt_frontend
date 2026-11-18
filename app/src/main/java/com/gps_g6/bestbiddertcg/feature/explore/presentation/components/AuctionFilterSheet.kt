package com.gps_g6.bestbiddertcg.feature.explore.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.rememberRangeSliderState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import com.gps_g6.bestbiddertcg.core.ui.theme.*

/**
 * Filtros de estado de subastas en la pantalla explore (Activo o Próximas).
 */
enum class AuctionStatusFilter {
    ACTIVE,
    UPCOMING
}

/**
 * Filtros de estado de conservación en la pantalla explore (Cualquiera, Nueva, Casi nueva, Buen estado, Con desgaste, Mala).
 */
enum class CardConditionFilter {
    ANY,
    NEW,
    NEAR_MINT,
    GOOD,
    LIGHT_PLAYED,
    POOR
}

private const val MAX_PRICE_SLIDER_VALUE = 250f

/**
 * Opciones de filtros en la pantalla explore.
 */
data class AuctionFilterOptions(
    val status: AuctionStatusFilter = AuctionStatusFilter.ACTIVE,
    val minPrice: Double = 0.0,
    val maxPrice: Double? = null,
    val condition: CardConditionFilter = CardConditionFilter.ANY,
    val endingWithinDays: Int? = 3
)

/**
 * Vista de filtros de subastas en la pantalla explore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionFilterSheet(
    initialFilters: AuctionFilterOptions = AuctionFilterOptions(),
    onDismiss: () -> Unit,
    onApply: (AuctionFilterOptions) -> Unit,
    showOnStart: Boolean = false
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(showOnStart) {
        if (showOnStart) {
            sheetState.show()
        }
    }

    var status by remember {
        mutableStateOf(initialFilters.status)
    }

    var minPrice by remember {
        mutableFloatStateOf(initialFilters.minPrice.toFloat())
    }

    var maxPrice by remember {
        mutableFloatStateOf(
            initialFilters.maxPrice?.toFloat() ?: MAX_PRICE_SLIDER_VALUE
        )
    }

    var condition by remember {
        mutableStateOf(initialFilters.condition)
    }

    var endingWithinDays by remember {
        mutableStateOf(initialFilters.endingWithinDays)
    }

    var isDateMenuExpanded by remember {
        mutableStateOf(false)
    }

    val dateSectionRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(isDateMenuExpanded) {
        if (isDateMenuExpanded) {
            dateSectionRequester.bringIntoView()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface,
        contentColor = TextPrimary,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(top = AppSpacing.s)
                    .size(width = 40.dp, height = 5.dp),
                color = Border,
                shape = CircleShape
            ) {}
        },
        shape = RoundedCornerShape(
            topStart = AppCornerRadius.large,
            topEnd = AppCornerRadius.large
        )
    ) {
        val view = LocalView.current
        val dialogWindow = (view.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.let { window ->
                WindowInsetsControllerCompat(window, view).apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 780.dp)
        ) {
            Text(
                text = "Filtrar subastas",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                modifier = Modifier.padding(
                    horizontal = AppSpacing.xl,
                    vertical = AppSpacing.l
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSpacing.s)
                    .background(Background)
            )

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AppSpacing.xl)
                    .padding(top = AppSpacing.l)
            ) {
                // Auction status
                FilterSectionTitle("Estado de la subasta")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.l)
                ) {
                    FilterOption(
                        text = "Activas",
                        selected = status == AuctionStatusFilter.ACTIVE,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppCornerRadius.small)
                    ) {
                        status = AuctionStatusFilter.ACTIVE
                    }

                    FilterOption(
                        text = "Próximas",
                        selected = status == AuctionStatusFilter.UPCOMING,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppCornerRadius.small)
                    ) {
                        status = AuctionStatusFilter.UPCOMING
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.l))

                FilterSectionTitle("Rango de precio")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.l)
                ) {
                    PriceInput(
                        label = "Mínimo",
                        value = minPrice,
                        modifier = Modifier.weight(1f)
                    ) {
                        minPrice = it.coerceIn(0f, maxPrice)
                    }

                    PriceInput(
                        label = "Máximo",
                        value = maxPrice,
                        showUnlimited = true,
                        modifier = Modifier.weight(1f)
                    ) {
                        maxPrice = it.coerceIn(minPrice, MAX_PRICE_SLIDER_VALUE)
                    }
                }

                val sliderState = rememberRangeSliderState(
                    activeRangeStart = minPrice,
                    activeRangeEnd = maxPrice,
                    valueRange = 0f..MAX_PRICE_SLIDER_VALUE
                )

                LaunchedEffect(minPrice, maxPrice) {
                    sliderState.activeRangeStart = minPrice
                    sliderState.activeRangeEnd = maxPrice
                }

                LaunchedEffect(sliderState) {
                    snapshotFlow {
                        sliderState.activeRangeStart..sliderState.activeRangeEnd
                    }.collect { range ->
                        minPrice = range.start
                        maxPrice = range.endInclusive
                    }
                }

                RangeSlider(
                    state = sliderState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppSpacing.s),
                    startThumb = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                                .border(2.dp, Primary, CircleShape)
                        )
                    },
                    endThumb = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                                .border(2.dp, Primary, CircleShape)
                        )
                    },
                    track = { state ->
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            val centerY = size.height / 2
                            val rangeSize =
                                state.valueRange.endInclusive - state.valueRange.start

                            val startFraction =
                                (state.activeRangeStart - state.valueRange.start) / rangeSize
                            val endFraction =
                                (state.activeRangeEnd - state.valueRange.start) / rangeSize

                            drawLine(
                                color = Border,
                                start = Offset(0f, centerY),
                                end = Offset(size.width, centerY),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )

                            drawLine(
                                color = Primary,
                                start = Offset(size.width * startFraction, centerY),
                                end = Offset(size.width * endFraction, centerY),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(AppSpacing.m))

                // Card condition
                FilterSectionTitle("Estado de conservación")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
                ) {
                    FilterOption(
                        text = "Cualquiera",
                        selected = condition == CardConditionFilter.ANY
                    ) {
                        condition = CardConditionFilter.ANY
                    }

                    FilterOption(
                        text = "Nueva",
                        selected = condition == CardConditionFilter.NEW
                    ) {
                        condition = CardConditionFilter.NEW
                    }

                    FilterOption(
                        text = "Como nueva",
                        selected = condition == CardConditionFilter.NEAR_MINT
                    ) {
                        condition = CardConditionFilter.NEAR_MINT
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
                ) {
                    FilterOption(
                        text = "Buen estado",
                        selected = condition == CardConditionFilter.GOOD
                    ) {
                        condition = CardConditionFilter.GOOD
                    }

                    FilterOption(
                        text = "Con marcas de uso",
                        selected = condition == CardConditionFilter.LIGHT_PLAYED
                    ) {
                        condition = CardConditionFilter.LIGHT_PLAYED
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
                ) {
                    FilterOption(
                        text = "Deteriorada",
                        selected = condition == CardConditionFilter.POOR
                    ) {
                        condition = CardConditionFilter.POOR
                    }
                }

                // Auction end date
                FilterSectionTitle("Finalización de la subasta")

                Spacer(modifier = Modifier.height(AppSpacing.m))


                Column(
                    modifier = Modifier.bringIntoViewRequester(dateSectionRequester)
                ) {
                    OutlinedButton(
                        onClick = {
                            isDateMenuExpanded = !isDateMenuExpanded
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        border = BorderStroke(1.dp, Border),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = when (endingWithinDays) {
                                0 -> "Hoy"
                                1 -> "En 1 día"
                                null -> "Cualquier fecha"
                                else -> "En los próximos $endingWithinDays días"
                            },
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = if (isDateMenuExpanded) "⌃" else "⌄",
                            color = TextSecondary
                        )
                    }

                    if (isDateMenuExpanded) {
                        Spacer(modifier = Modifier.height(AppSpacing.xs))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    Border,
                                    RoundedCornerShape(AppCornerRadius.medium)
                                )
                                .background(
                                    Surface,
                                    RoundedCornerShape(AppCornerRadius.medium)
                                )
                        ) {
                            listOf(
                                0 to "Hoy",
                                1 to "En 1 día",
                                2 to "En los próximos 2 días",
                                3 to "En los próximos 3 días",
                                4 to "En los próximos 4 días",
                                5 to "En los próximos 5 días",
                                6 to "En los próximos 6 días",
                                7 to "En los próximos 7 días"
                            ).forEach { (days, label) ->
                                Surface(
                                    onClick = {
                                        endingWithinDays = days
                                        isDateMenuExpanded = false
                                    },
                                    color = Surface,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = label,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .padding(horizontal = AppSpacing.l),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.xl))
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSpacing.s)
                    .background(Background)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppSpacing.xl,
                        vertical = AppSpacing.l
                    ),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
            ) {
                OutlinedButton(
                    onClick = {
                        status = AuctionStatusFilter.ACTIVE
                        minPrice = 0f
                        maxPrice = MAX_PRICE_SLIDER_VALUE
                        condition = CardConditionFilter.ANY
                        endingWithinDays = 3
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    border = BorderStroke(1.dp, Primary)
                ) {
                    Text(
                        text = "Limpiar",
                        color = Primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = {
                        onApply(
                            AuctionFilterOptions(
                                status = status,
                                minPrice = minPrice.toDouble(),
                                maxPrice = maxPrice
                                    .takeIf { it < MAX_PRICE_SLIDER_VALUE }
                                    ?.toDouble(),
                                condition = condition,
                                endingWithinDays = endingWithinDays
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Text(
                        text = "Aplicar filtros",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

/**
 * Titulo de seccion de filtros en la pantalla explore.
 */
@Composable
private fun FilterSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        ),
        color = TextPrimary
    )
}

/**
 * Opcion de filtro en la pantalla explore.
 */
@Composable
private fun FilterOption(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = shape,
        color = if (selected) PrimaryLight else Surface,
        border = BorderStroke(
            1.dp,
            if (selected) Primary else Border
        )
    ) {
        androidx.compose.foundation.layout.Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = AppSpacing.m),
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) Primary else TextPrimary
            )
        }
    }
}

/**
 * Campo de entrada de precio en la pantalla explore.
 */
@Composable
private fun PriceInput(
    label: String,
    value: Float,
    modifier: Modifier = Modifier,
    showUnlimited: Boolean = false,
    onValueChange: (Float) -> Unit
) {
    var text by remember {
        mutableStateOf(
            if (showUnlimited && value >= MAX_PRICE_SLIDER_VALUE) {
                "Sin límite"
            } else {
                value.toInt().toString()
            }
        )
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(value, isFocused) {
        if (!isFocused) {
            text = if (showUnlimited && value >= MAX_PRICE_SLIDER_VALUE) {
                "Sin límite"
            } else {
                "${value.toInt()} €"
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(
                start = AppSpacing.s,
                bottom = AppSpacing.xs
            )
        )

        OutlinedTextField(
            value = text,
            onValueChange = { input ->
                text = input
                input.replace("€", "")
                    .trim()
                    .toFloatOrNull()
                    ?.let(onValueChange)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center
            ),
            shape = RoundedCornerShape(AppCornerRadius.medium)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Auction Filter Sheet"
)
@Composable
private fun AuctionFilterSheetPreview() {
    BestBidderTCGTheme {
        AuctionFilterSheet(
            initialFilters = AuctionFilterOptions(),
            onDismiss = {},
            onApply = {},
            showOnStart = true
        )
    }
}