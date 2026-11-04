package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.AppSpacing
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface as AppSurface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.DropdownField
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.EstimatedEndDate
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.FormCard
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.FormInput
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SearchDropdownField
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SectionTitle
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.SelectionPlaceholder
import com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components.DurationSelector

/**
 * Pantalla de creacion de subastas.
 */
@Composable
fun CreateAuctionScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf("collection") }

    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardDescription by remember { mutableStateOf("") }

    var startingPrice by remember { mutableStateOf("") }
    var auctionDescription by remember { mutableStateOf("") }

    var selectedDuration by remember { mutableIntStateOf(3) }

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

                    SelectionPlaceholder(
                        icon = R.drawable.ic_grid,
                        title = "Seleccionar de mi colección",
                        description = "Elige una carta que ya tengas registrada"
                    )

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
                    value = startingPrice,
                    onValueChange = { startingPrice = it },
                    placeholder = "0,00"
                )

                FormInput(
                    label = "Descripción de la subasta (opcional)",
                    value = auctionDescription,
                    onValueChange = { auctionDescription = it },
                    placeholder = "Añade información sobre la subasta...",
                    multiline = true
                )
            }

            // Auction schedule
            FormCard {

                SectionTitle("Programación")

                Spacer(modifier = Modifier.height(AppSpacing.m))

                DateTimeField()

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Text(
                    text = "Intervalos de 15 minutos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(AppSpacing.xl))

                DurationSelector(
                    selectedDuration = selectedDuration,
                    onDurationSelected = {
                        selectedDuration = it
                    }
                )

                Spacer(modifier = Modifier.height(AppSpacing.xxl))

                EstimatedEndDate()
            }
        }

        // Fixed bottom button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppSurface,
            shadowElevation = 4.dp
        ) {
            Button(
                onClick = {
                    // TODO: Create auction
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppSpacing.l,
                        vertical = AppSpacing.m
                    )
                    .height(60.dp),
                shape = RoundedCornerShape(AppCornerRadius.medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = AppSurface
                )
            ) {
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

/**
 * Campo de fecha y hora de inicio.
 */
@Composable
private fun DateTimeField() {
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
                .background(
                    AppSurface,
                    RoundedCornerShape(AppCornerRadius.large)
                )
                .padding(horizontal = AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Selecciona fecha y hora",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
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