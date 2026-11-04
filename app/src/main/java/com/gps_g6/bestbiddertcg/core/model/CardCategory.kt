package com.gps_g6.bestbiddertcg.core.model

import androidx.annotation.StringRes
import com.gps_g6.bestbiddertcg.R

// TODO: MIRAR CON BACKEND LOS IDS DE LAS CATEGORÍAS (INT)
enum class CardCategory(
    val id: String,
    @StringRes val displayNameRes: Int
) {
    POKEMON(
        id = "POKEMON",
        displayNameRes = R.string.preferences_pokemon
    ),
    MAGIC(
        id = "MAGIC",
        displayNameRes = R.string.preferences_magic
    ),
    YUGIOH(
        id = "YUGIOH",
        displayNameRes = R.string.preferences_yugioh
    ),
    ONE_PIECE(
        id = "ONE_PIECE",
        displayNameRes = R.string.preferences_onepiece
    ),
    DRAGON_BALL(
        id = "DRAGON_BALL",
        displayNameRes = R.string.preferences_dragonball
    )
}
