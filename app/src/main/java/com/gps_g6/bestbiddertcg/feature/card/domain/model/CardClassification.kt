package com.gps_g6.bestbiddertcg.feature.card.domain.model

data class CardClassification(
    val category: CardCategory? = null,
    val collection: CardCollection? = null
) {
    init {
        require(collection == null || collection.category == category) {
            "La colección debe pertenecer a la categoría seleccionada"
        }
    }
}
