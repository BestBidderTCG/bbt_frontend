package com.gps_g6.bestbiddertcg.feature.card.domain.model

import androidx.annotation.StringRes
import com.gps_g6.bestbiddertcg.R

enum class CardCollection(
    val apiValue: String,
    @param:StringRes val labelRes: Int,
    val category: CardCategory
) {
    DESTINOS_DE_PALDEA(
        "Destinos de Paldea",
        R.string.card_collection_destinos_de_paldea,
        CardCategory.POKEMON
    ),
    BRECHA_PARADOJICA(
        "Brecha Paradojica",
        R.string.card_collection_brecha_paradojica,
        CardCategory.POKEMON
    ),
    POKEMON_151(
        "Pokemon 151",
        R.string.card_collection_pokemon_151,
        CardCategory.POKEMON
    ),
    EVOLUCIONES_DE_PALDEA(
        "Evoluciones de Paldea",
        R.string.card_collection_evoluciones_de_paldea,
        CardCategory.POKEMON
    ),
    SURGING_SPARKS(
        "Surging Sparks",
        R.string.card_collection_surging_sparks,
        CardCategory.POKEMON
    ),
    RIFTBOUND_ORIGINS(
        "Riftbound: Origins",
        R.string.card_collection_riftbound_origins,
        CardCategory.RIFTBOUND
    ),
    SPIRITFORGED(
        "Spiritforged",
        R.string.card_collection_spiritforged,
        CardCategory.RIFTBOUND
    ),
    UNLEASHED(
        "Unleashed",
        R.string.card_collection_unleashed,
        CardCategory.RIFTBOUND
    ),
    ALPHA_BETA_UNLIMITED(
        "Alpha / Beta / Unlimited (1993)",
        R.string.card_collection_alpha_beta_unlimited,
        CardCategory.MAGIC
    ),
    CICLO_DE_URZA(
        "Ciclo de Urza (1998-1999)",
        R.string.card_collection_ciclo_de_urza,
        CardCategory.MAGIC
    ),
    WILDS_OF_ELDRAINE(
        "Wilds of Eldraine",
        R.string.card_collection_wilds_of_eldraine,
        CardCategory.MAGIC
    );

    companion object {
        fun forCategory(category: CardCategory): List<CardCollection> =
            entries.filter { it.category == category }

        fun fromApi(value: String): CardCollection =
            entries.firstOrNull {
                it.apiValue.equals(value, ignoreCase = true)
            } ?: throw IllegalArgumentException(
                "Colección desconocida: $value"
            )
    }
}