package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Error as ErrorColor
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.data.AuctionModule
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.CardOptionRow
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.CardPickerDialog
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.CardSelectionList
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.DropdownField
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.DurationSelector
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.EstimatedEndDate
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.FormCard
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.FormInput
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SearchDropdownField
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SectionTitle
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SelectionPlaceholder
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.StartDatePickerDialog
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.StartTimePickerDialog
import com.gps_g6.bestbiddertcg.feature.card.data.CardModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm")

/**
 * Pantalla de creacion de subastas.
 */
@Composable
fun CreateAuctionScreen(
    onBackClick: () -> Unit = {},
    onAuctionCreated: () -> Unit = {}
) {

    val viewModel: CreateAuctionViewModel = viewModel(
        factory = CreateAuctionViewModelFactory(
            createAuctionUseCase = AuctionModule.createAuctionUseCase,
            getMyCardsUseCase = CardModule.getMyCardsUseCase
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    var selectedMode by remember { mutableStateOf("collection") }

    // Campos de "Nueva carta" (pendientes de implementar).
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardDescription by remember { mutableStateOf("") }

    // Selector de fecha y hora (primero fecha, luego hora).
    var showDatePicker by remember { mutableStateOf(false) }
    var showCardPicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pickedDate by remember { mutableStateOf<LocalDate?>(null) }

    // Reinicia el formulario y carga las cartas al abrir la pantalla.
    LaunchedEffect(Unit) {
        viewModel.start()
    }

    // Cuando la subasta se crea correctamente, se cierra la pantalla.
    LaunchedEffect(uiState.createdAuction) {
        if (uiState.createdAuction != null) {
            viewModel.onAuctionCreatedHandled()
            onAuctionCreated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryLight)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(AppSurface)
                .padding(horizontal = AppSpacing.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_left_arrow),
                contentDescription = "Volver",
                tint = TextPrimary,
                modifier = Modifier
                    .size(26.dp)
                    .clickable(onClick = onBackClick)
            )

            Spacer(modifier = Modifier.width(AppSpacing.xl))

            Text(
                text = "Crear subasta",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppSpacing.l,
                    vertical = AppSpacing.l
                ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)
        ) {

            // Card selection
            FormCard {

                SectionTitle("Carta a subastar")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                // Mode selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            PrimaryLight,
                            RoundedCornerShape(AppCornerRadius.medium)
                        )
                        .padding(AppSpacing.s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuctionModeTab(
                        text = "Mi colección",
                        selected = selectedMode == "collection",
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedMode = "collection"
                    }

                    AuctionModeTab(
                        text = "Nueva carta",
                        selected = selectedMode == "new",
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedMode = "new"
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.l))

                if (selectedMode == "collection") {

                    val selectedCard = uiState.selectedCard

                    if (selectedCard == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(AppCornerRadius.medium))
                                .clickable {
                                    viewModel.loadCards()
                                    showCardPicker = true
                                }
                        ) {
                            SelectionPlaceholder(
                                icon = R.drawable.ic_grid,
                                title = "Seleccionar de mi colección",
                                description = "Elige una carta que ya tengas registrada"
                            )
                        }
                    } else {
                        CardOptionRow(
                            card = selectedCard,
                            selected = true,
                            onClick = {
                                viewModel.loadCards()
                                showCardPicker = true
                            }
                        )
                    }

                } else {

                    SelectionPlaceholder(
                        icon = R.drawable.ic_add_image,
                        title = "Añadir imagen",
                        description = "Selecciona una imagen de la carta"
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.l))

                    FormInput(
                        label = "Nombre de la carta",
                        value = cardName,
                        onValueChange = { cardName = it },
                        placeholder = "Ej. Piplup"
                    )

                    DropdownField(
                        label = "Categoría",
                        placeholder = "Selecciona una categoría"
                    )

                    SearchDropdownField(
                        label = "Colección (opcional)",
                        placeholder = "Buscar colección..."
                    )

                    FormInput(
                        label = "Numeración (opcional)",
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        placeholder = "Ej. 001"
                    )

                    DropdownField(
                        label = "Estado de conservación",
                        placeholder = "Seleccionar estado"
                    )

                    FormInput(
                        label = "Descripción de la carta (opcional)",
                        value = cardDescription,
                        onValueChange = { cardDescription = it },
                        placeholder = "Información adicional...",
                        multiline = true
                    )
                }
            }

            // Auction details
            FormCard {

                SectionTitle("Detalles de la subasta")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                FormInput(
                    label = "Precio inicial (€)",
                    value = uiState.startingPrice,
                    onValueChange = viewModel::updateStartingPrice,
                    placeholder = "0,00",
                    keyboardType = KeyboardType.Decimal
                )

                FormInput(
                    label = "Descripción de la subasta (opcional)",
                    value = uiState.description,
                    onValueChange = viewModel::updateDescription,
                    placeholder = "Añade información sobre la subasta...",
                    multiline = true
                )
            }

            // Auction schedule
            FormCard {

                SectionTitle("Programación")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                DateTimeField(
                    value = dateTimeFormatter.format(uiState.startDateTime),
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Text(
                    text = "Intervalos de 15 minutos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(AppSpacing.xl))

                DurationSelector(
                    selectedDuration = uiState.durationInDays,
                    onDurationSelected = viewModel::updateDuration
                )

                Spacer(modifier = Modifier.height(AppSpacing.xxl))

                EstimatedEndDate(
                    dateText = dateTimeFormatter.format(
                        uiState.startDateTime.plusDays(uiState.durationInDays.toLong())
                    )
                )
            }
        }

        // Fixed bottom area
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppSurface,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = AppSpacing.l,
                    vertical = AppSpacing.m
                )
            ) {

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ErrorColor,
                        modifier = Modifier.padding(bottom = AppSpacing.s)
                    )
                }

                Button(
                    onClick = {
                        if (selectedMode == "collection") {
                            viewModel.createAuction()
                        } else {
                            viewModel.showError(
                                "Crear cartas nuevas aún no está disponible. Usa «Mi colección»."
                            )
                        }
                    },
                    enabled = !uiState.isCreating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = AppSurface
                    )
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = AppSurface,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Crear subasta",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        StartDatePickerDialog(
            initialDate = uiState.startDateTime.toLocalDate(),
            onDateSelected = { date ->
                pickedDate = date
                showDatePicker = false
                showTimePicker = true
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        StartTimePickerDialog(
            initialTime = uiState.startDateTime.toLocalTime(),
            onTimeSelected = { time ->
                viewModel.updateStartDateTime(
                    date = pickedDate ?: uiState.startDateTime.toLocalDate(),
                    time = time
                )
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showCardPicker) {
        CardPickerDialog(
            cards = uiState.cards,
            selectedCardId = uiState.selectedCard?.id,
            isLoading = uiState.isLoadingCards,
            errorMessage = uiState.cardsError,
            onCardSelected = { card ->
                viewModel.selectCard(card)
                showCardPicker = false
            },
            onRetry = viewModel::loadCards,
            onDismiss = { showCardPicker = false }
        )
    }
}

/**
 * Campo de fecha y hora de inicio.
 */
@Composable
private fun DateTimeField(
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Fecha y hora de inicio",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(AppCornerRadius.large))
                .background(AppSurface)
                .clickable(onClick = onClick)
                .padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = "Seleccionar fecha y hora",
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Pestaña de seleccion del modo de carta.
 */
@Composable
private fun AuctionModeTab(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                if (selected) {
                    PrimaryLight
                } else {
                    Color.Transparent
                },
                RoundedCornerShape(AppCornerRadius.small)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                }
            ),
            color = if (selected) Primary else TextSecondary
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateAuctionScreenPreview() {
    BestBidderTCGTheme {
        CreateAuctionScreen()
    }
}