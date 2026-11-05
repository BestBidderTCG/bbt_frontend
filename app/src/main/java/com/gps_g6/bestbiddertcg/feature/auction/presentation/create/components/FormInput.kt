package com.gps_g6.bestbiddertcg.feature.auction.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextPrimary
import com.gps_g6.bestbiddertcg.core.ui.theme.AppCornerRadius
import com.gps_g6.bestbiddertcg.core.ui.theme.Border
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary
import androidx.compose.material3.MaterialTheme

@Composable
fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    multiline: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
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
                .clip(RoundedCornerShape(AppCornerRadius.large))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(AppCornerRadius.large)
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
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
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = TextPrimary
                    ),
                    singleLine = !multiline,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                )
            }
        }
    }
}