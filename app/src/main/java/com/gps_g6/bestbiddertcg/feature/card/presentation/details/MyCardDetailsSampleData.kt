package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus

object MyCardDetailsSampleData {

    val available = MyCardDetailsUiModel(
        id = "card-1",
        name = "Piplup",
        imageUrl = null,
        category = "Pokemon",
        collection = "Pokemon 151",
        number = 393,
        condition = "GOOD",
        description = "Carta Piplup monísima, con leves signos de uso en los bordes.",
        publishedAt = "2026-10-12T14:20:00",
        availability = CardAvailability.Available
    )

    val activeAuction = available.copy(
        availability = CardAvailability.InAuction(
            auctionId = "auction-1",
            status = AuctionStatus.ACTIVE
        )
    )

    val scheduledAuction = available.copy(
        availability = CardAvailability.InAuction(
            auctionId = "auction-2",
            status = AuctionStatus.UPCOMING
        )
    )
}
