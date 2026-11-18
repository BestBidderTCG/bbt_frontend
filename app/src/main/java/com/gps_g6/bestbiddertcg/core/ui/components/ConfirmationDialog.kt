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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    isConfirmEnabled: Boolean = true
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Primary
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary
                    )
                ) {
                    Text(
                        text = cancelText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = onConfirm,
                    enabled = isConfirmEnabled,
                    modifier = Modifier
                        .widthIn(min = 120.dp)
                        .wrapContentWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(AppCornerRadius.medium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Surface
                    )
                ) {
                    Text(
                        text = confirmText,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        softWrap = false,
                        textAlign = TextAlign.Center
                    )
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
