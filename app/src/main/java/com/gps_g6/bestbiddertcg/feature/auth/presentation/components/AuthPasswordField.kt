package com.gps_g6.bestbiddertcg.feature.auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary
            ),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            shape = RoundedCornerShape(AppCornerRadius.medium),
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) {
                                R.drawable.ic_visibility
                            } else {
                                R.drawable.ic_visibility_off
                            }
                        ),
                        contentDescription = stringResource(
                            if (passwordVisible) {
                                R.string.auth_hide_password
                            } else {
                                R.string.auth_show_password
                            }
                        ),
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                cursorColor = Primary,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPasswordFieldPreview() {
    BestBidderTCGTheme {
        var password by remember { mutableStateOf("") }

        Box(
            modifier = Modifier.padding(AppSpacing.l)
        ) {
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña *",
                placeholder = "Introduce tu contraseña"
            )
        }
    }
}