package com.gps_g6.bestbiddertcg.feature.profile.presentation

object ProfileSampleData {

    val profile = ProfileUiState(
        username = "EntrenadoraTCG",
        email = "coleccionista@example.com",
        cardsCount = 24,
        auctionsCount = 5,
        bidsCount = 12,
        collection = (1..24).map { index ->
            CollectionCardUiModel(
                id = index.toString(),
                name = "Piplup"
            )
        },
        auctions = listOf(
            AuctionUiModel(
                id = "1",
                cardName = "Piplup",
                price = "45,00 €",
                status = AuctionStatus.ACTIVE,
                detail = "2 h 35 min"
            ),
            AuctionUiModel(
                id = "2",
                cardName = "Piplup",
                price = "18,00 €",
                status = AuctionStatus.UPCOMING,
                detail = "Inicio: 30/10/2026, 18h00"
            ),
            AuctionUiModel(
                id = "3",
                cardName = "Piplup",
                price = "28,00 €",
                status = AuctionStatus.FINISHED,
                detail = "Best Bidder: @ArianaGrande"
            )
        )
    )

}