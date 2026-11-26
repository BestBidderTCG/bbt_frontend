package com.gps_g6.bestbiddertcg.feature.card.presentation.details

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardAuction
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetCardDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MyCardDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCardDetailsUseCase: GetCardDetailsUseCase

    private val testCard = Card(
        id = "card-1",
        ownerId = "user-1",
        name = "Piplup",
        image = "/uploads/piplup.jpg",
        category = CardCategory.POKEMON,
        collection = CardCollection.POKEMON_151,
        num = 393,
        state = CardCondition.GOOD,
        description = "Carta de prueba",
        createdAt = "2026-11-21T17:10:54Z",
        auction = null
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCardDetailsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = MyCardDetailsViewModel(
        cardId = "card-1",
        getCardDetailsUseCase = getCardDetailsUseCase
    )

    @Test
    fun `initial state is loading`() = runTest(testDispatcher) {
        coEvery {
            getCardDetailsUseCase("card-1")
        } returns testCard

        val viewModel = createViewModel()

        assertEquals(
            MyCardDetailsUiState.Loading,
            viewModel.uiState.value
        )

        advanceUntilIdle()
    }

    @Test
    fun `loads card details correctly`() = runTest(testDispatcher) {
        coEvery {
            getCardDetailsUseCase("card-1")
        } returns testCard

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MyCardDetailsUiState.Success)

        val card = (state as MyCardDetailsUiState.Success).card

        assertEquals("card-1", card.id)
        assertEquals("Piplup", card.name)
        assertEquals(
            "http://10.0.2.2:8080/uploads/piplup.jpg",
            card.imageUrl
        )
        assertEquals(CardCategory.POKEMON, card.category)
        assertEquals(CardCollection.POKEMON_151, card.collection)
        assertEquals(393, card.number)
        assertEquals(CardCondition.GOOD, card.condition)
        assertEquals("Carta de prueba", card.description)
        assertEquals(
            "2026-11-21T17:10:54Z",
            card.publishedAt
        )
        assertEquals(
            CardAvailability.Available,
            card.availability
        )

        coVerify(exactly = 1) {
            getCardDetailsUseCase("card-1")
        }
    }

    @Test
    fun `active auction marks card as unavailable`() =
        runTest(testDispatcher) {
            val cardInAuction = testCard.copy(
                auction = CardAuction(
                    id = "auction-1",
                    status = AuctionStatus.ACTIVE
                )
            )

            coEvery {
                getCardDetailsUseCase("card-1")
            } returns cardInAuction

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value
                    as MyCardDetailsUiState.Success

            assertEquals(
                CardAvailability.InAuction(
                    auctionId = "auction-1",
                    status = AuctionStatus.ACTIVE
                ),
                state.card.availability
            )
        }

    @Test
    fun `upcoming auction marks card as unavailable`() =
        runTest(testDispatcher) {
            val cardInAuction = testCard.copy(
                auction = CardAuction(
                    id = "auction-2",
                    status = AuctionStatus.UPCOMING
                )
            )

            coEvery {
                getCardDetailsUseCase("card-1")
            } returns cardInAuction

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value
                    as MyCardDetailsUiState.Success

            assertEquals(
                CardAvailability.InAuction(
                    auctionId = "auction-2",
                    status = AuctionStatus.UPCOMING
                ),
                state.card.availability
            )
        }

    @Test
    fun `finished auction does not block card`() =
        runTest(testDispatcher) {
            val cardWithFinishedAuction = testCard.copy(
                auction = CardAuction(
                    id = "auction-3",
                    status = AuctionStatus.FINISHED
                )
            )

            coEvery {
                getCardDetailsUseCase("card-1")
            } returns cardWithFinishedAuction

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value
                    as MyCardDetailsUiState.Success

            assertEquals(
                CardAvailability.Available,
                state.card.availability
            )
        }

    @Test
    fun `network error shows error state`() =
        runTest(testDispatcher) {
            coEvery {
                getCardDetailsUseCase("card-1")
            } throws IOException("Network unavailable")

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                MyCardDetailsUiState.Error,
                viewModel.uiState.value
            )
        }

    @Test
    fun `retry after error loads card successfully`() =
        runTest(testDispatcher) {
            coEvery {
                getCardDetailsUseCase("card-1")
            } throws IOException("Network unavailable") andThen testCard

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                MyCardDetailsUiState.Error,
                viewModel.uiState.value
            )

            viewModel.loadCard()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is MyCardDetailsUiState.Success)

            coVerify(exactly = 2) {
                getCardDetailsUseCase("card-1")
            }
        }
}
