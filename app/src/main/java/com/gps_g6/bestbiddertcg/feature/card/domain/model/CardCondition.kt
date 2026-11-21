package com.gps_g6.bestbiddertcg.feature.card.domain.model

import androidx.annotation.StringRes
import com.gps_g6.bestbiddertcg.R

enum class CardCondition(
    val apiValue: String,
    @param:StringRes val labelRes: Int
) {
    NEW("NEW", R.string.card_condition_new),
    NEAR_MINT("NEAR_MINT", R.string.card_condition_near_mint),
    GOOD("GOOD", R.string.card_condition_good),
    LIGHT_PLAYED("LIGHT_PLAYED", R.string.card_condition_light_played),
    POOR("POOR", R.string.card_condition_poor);

    companion object {
        fun fromApi(value: String): CardCondition =
            entries.firstOrNull { it.apiValue == value }
                ?: throw IllegalArgumentException(
                    "Estado de conservación desconocido: $value"
                )
    }
}