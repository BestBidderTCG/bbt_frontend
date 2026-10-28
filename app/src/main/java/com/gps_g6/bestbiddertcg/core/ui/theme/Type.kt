
package com.gps_g6.bestbiddertcg.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.gps_g6.bestbiddertcg.R

private val AppFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    headlineLarge = Typography().headlineLarge.copy(
        fontFamily = AppFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = Typography().headlineMedium.copy(
        fontFamily = AppFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontFamily = AppFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = Typography().bodyMedium.copy(
        fontFamily = AppFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    labelMedium = Typography().labelMedium.copy(
        fontFamily = AppFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodySmall = Typography().bodySmall.copy(
        fontFamily = AppFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
)
