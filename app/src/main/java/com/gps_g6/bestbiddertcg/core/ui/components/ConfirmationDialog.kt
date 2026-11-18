package com.gps_g6.bestbiddertcg.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    cancelText: String,
    isConfirmEnabled: Boolean = true,
    buttonColor: Color = Primary
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(AppCornerRadius.large)
                )
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.large)
                )
                .padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            val textMeasurer = rememberTextMeasurer()
            val buttonTextStyle = MaterialTheme.typography.labelMedium
            val buttonSpacing = AppSpacing.s
            val minButtonWidth = 96.dp

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val density = androidx.compose.ui.platform.LocalDensity.current

                val cancelTextWidth = with(density) {
                    textMeasurer.measure(
                        text = cancelText,
                        style = buttonTextStyle,
                        maxLines = 1
                    ).size.width.toDp()
                }

                val confirmTextWidth = with(density) {
                    textMeasurer.measure(
                        text = confirmText,
                        style = buttonTextStyle,
                        maxLines = 1
                    ).size.width.toDp()
                }

                // Espacio horizontal que Material 3 reserva
                // dentro de cada botón.
                val horizontalButtonPadding = 48.dp

                val cancelRequiredWidth = maxOf(
                    minButtonWidth,
                    cancelTextWidth + horizontalButtonPadding
                )

                val confirmRequiredWidth = maxOf(
                    minButtonWidth,
                    confirmTextWidth + horizontalButtonPadding
                )

                val availableWidth = maxWidth - buttonSpacing
                val equalWidth = availableWidth / 2

                val useEqualWidths =
                    equalWidth >= cancelRequiredWidth &&
                            equalWidth >= confirmRequiredWidth

                val confirmWidth = if (useEqualWidths) {
                    equalWidth
                } else {
                    minOf(
                        confirmRequiredWidth,
                        availableWidth - cancelRequiredWidth
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            buttonColor
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = buttonColor
                        )
                    ) {
                        Text(
                            text = cancelText,
                            style = buttonTextStyle,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = isConfirmEnabled,
                        modifier = Modifier
                            .width(confirmWidth)
                            .height(48.dp),
                        shape = RoundedCornerShape(AppCornerRadius.medium),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Surface
                        )
                    ) {
                        Text(
                            text = confirmText,
                            style = buttonTextStyle,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationDialogPreview() {
    BestBidderTCGTheme {
        ConfirmationDialog(
            title = "¿Quieres cerrar sesión?",
            message = "Tendrás que iniciar sesión de nuevo para acceder a tu cuenta",
            confirmText = "Cerrar sesión",
            cancelText = "Cancelar",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
