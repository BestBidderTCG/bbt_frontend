package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

private val CardBackground = Color(0xFFFFF9F5)
private val FieldBorder = Color(0xFFE1E5EB)
private val MutedText = Color(0xFF737D91)

/**
 * Pantalla de creacion de subastas.
 */
@Composable
fun CreateAuctionScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf("collection") }

    var cardName by remember { mutableStateOf("") }
    var cardDescription by remember { mutableStateOf("") }
    var startingPrice by remember { mutableStateOf("") }
    var auctionDescription by remember { mutableStateOf("") }

    var selectedDuration by remember { mutableIntStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBackground)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(Surface)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_left_arrow),
                contentDescription = "Volver",
                tint = Color.Black,
                modifier = Modifier
                    .size(26.dp)
                    .clickable(onClick = onBackClick)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "Crear subasta",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Card information
            FormCard {

                SectionTitle("Carta a subastar")

                Spacer(modifier = Modifier.height(12.dp))

                // Mode selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .padding(8.dp),
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

                Spacer(modifier = Modifier.height(16.dp))

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

                    Spacer(modifier = Modifier.height(16.dp))

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
                        value = "",
                        onValueChange = {},
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

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(12.dp))

                DateTimeField()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Intervalos de 15 minutos",
                    fontSize = 14.sp,
                    color = MutedText
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Duración",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    (1..7).forEach { day ->
                        DurationItem(
                            day = day,
                            selected = selectedDuration == day,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedDuration = day
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Duración en días (1-7)",
                    fontSize = 14.sp,
                    color = MutedText,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                EstimatedEndDate()
            }
        }

        // Fixed bottom button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Surface,
            shadowElevation = 4.dp
        ) {
            Button(
                onClick = {
                    // TODO: Create auction
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                    .height(60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Surface
                )
            ) {
                Text(
                    text = "Crear subasta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Contenedor de las tarjetas del formulario.
 */
@Composable
private fun FormCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Surface,
        border = BorderStroke(1.dp, FieldBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

/**
 * Titulo de cada seccion.
 */
@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )
}

/**
 * Panel para seleccionar una carta o añadir una imagen.
 */
@Composable
private fun SelectionPlaceholder(
    icon: Int,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = FieldBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = MutedText,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Campo de texto del formulario.
 */
@Composable
private fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    multiline: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (multiline) 96.dp else 60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = FieldBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = if (multiline) {
                Alignment.Top
            } else {
                Alignment.CenterVertically
            }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = MutedText
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = TextPrimary
                    ),
                    singleLine = !multiline
                )
            }
        }
    }
}

/**
 * Campo visual de seleccion desplegable.
 */
@Composable
private fun DropdownField(
    label: String,
    placeholder: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = FieldBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = MutedText,
                modifier = Modifier.weight(1f)
            )

            ChevronDown()
        }
    }
}

/**
 * Campo de busqueda de colecciones.
 */
@Composable
private fun SearchDropdownField(
    label: String,
    placeholder: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = FieldBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchSymbol()

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = MutedText,
                modifier = Modifier.weight(1f)
            )

            ChevronDown()
        }
    }
}

/**
 * Icono de busqueda.
 */
@Composable
private fun SearchSymbol() {
    androidx.compose.foundation.Canvas(
        modifier = Modifier.size(24.dp)
    ) {
        drawCircle(
            color = MutedText,
            radius = size.width * 0.30f,
            center = androidx.compose.ui.geometry.Offset(
                size.width * 0.42f,
                size.height * 0.42f
            ),
            style = Stroke(width = 2.dp.toPx())
        )

        drawLine(
            color = MutedText,
            start = androidx.compose.ui.geometry.Offset(
                size.width * 0.64f,
                size.height * 0.64f
            ),
            end = androidx.compose.ui.geometry.Offset(
                size.width * 0.90f,
                size.height * 0.90f
            ),
            strokeWidth = 2.dp.toPx()
        )
    }
}

/**
 * Icono de desplegable.
 */
@Composable
private fun ChevronDown() {
    androidx.compose.foundation.Canvas(
        modifier = Modifier.size(20.dp)
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.35f)
            lineTo(size.width * 0.5f, size.height * 0.65f)
            lineTo(size.width * 0.8f, size.height * 0.35f)
        }

        drawPath(
            path = path,
            color = MutedText,
            style = Stroke(
                width = 2.dp.toPx()
            )
        )
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
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = FieldBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Selecciona fecha y hora",
                fontSize = 16.sp,
                color = MutedText,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = "Seleccionar fecha y hora",
                tint = MutedText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Selector de duracion en dias.
 */
@Composable
private fun DurationItem(
    day: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) PrimaryLight else Surface
            )
            .border(
                width = 1.dp,
                color = if (selected) Primary else FieldBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 16.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (selected) Primary else MutedText
        )
    }
}

/**
 * Fecha estimada de finalizacion.
 */
@Composable
private fun EstimatedEndDate() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Finalización estimada",
            fontSize = 14.sp,
            color = MutedText
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "15/11/2026, 12h45",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
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
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) PrimaryLight else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (selected) Primary else MutedText
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