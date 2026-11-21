package com.gps_g6.bestbiddertcg.feature.card.domain.model

import androidx.annotation.StringRes
import com.gps_g6.bestbiddertcg.R

enum class CardCategory(
    val apiValue: String,
    @param:StringRes val labelRes: Int
) {
    POKEMON("Pokémon", R.string.card_category_pokemon),
    RIFTBOUND("Riftbound", R.string.card_category_riftbound),
    MAGIC("Magic", R.string.card_category_magic);

    companion object {
        fun fromApi(value: String): CardCategory =
            entries.firstOrNull {
                it.apiValue.equals(value, ignoreCase = true)
            } ?: throw IllegalArgumentException(
                "Categoría desconocida: $value"
            )
    }
}