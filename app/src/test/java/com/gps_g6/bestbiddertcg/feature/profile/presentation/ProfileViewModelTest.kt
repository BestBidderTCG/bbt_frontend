
package com.gps_g6.bestbiddertcg.feature.profile.presentation

import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardSummary
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var profileManager: ProfileManager
    private lateinit var getMyCardsUseCase: GetMyCardsUseCase

    private val profileFlow = MutableStateFlow<Profile?>(null)

    private val testProfile = Profile(
        id = "user-1",
        username = "testuser",
        email = "test@example.com",
        description = "Mi colección",
        avatarNum = 1,
        createdAt = "2026-11-05T12:00:00"
    )

    private fun card(
        id: String,
        image: String =
            "https://res.cloudinary.com/ee6azugw/image/upload/bestbidder/$id.jpg",
        category: CardCategory = CardCategory.POKEMON,
        inAuction: Boolean = false
    ) = CardSummary(
        id = id,
        name = "Carta $id",
        image = image,
        category = category,
        inAuction = inAuction
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        profileFlow.value = null
        profileManager = mockk()
        getMyCardsUseCase = mockk()

        every {
            profileManager.profile
        } returns profileFlow

        coEvery {
            profileManager.loadProfile(any())
        } returns testProfile

        coEvery {
            getMyCardsUseCase.invoke()
        } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = ProfileViewModel(
        profileManager = profileManager,
        getMyCardsUseCase = getMyCardsUseCase
    )

    @Test
    fun `initial load displays collection and updates card count`() =
        runTest(testDispatcher) {
            coEvery {
                getMyCardsUseCase.invoke()
            } returns listOf(
                card("card-1"),
                card("card-2")
            )

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(2, state.collection.size)
            assertEquals(2, state.cardsCount)
            assertEquals("card-1", state.collection[0].id)
            assertEquals("Carta card-1", state.collection[0].name)
            assertEquals(
                "https://res.cloudinary.com/ee6azugw/image/upload/bestbidder/card-1.jpg",
                state.collection[0].imageUrl
            )

            assertFalse(state.isLoadingCards)
            assertNull(state.cardsError)

            coVerify(exactly = 1) {
                getMyCardsUseCase.invoke()
            }
        }

    @Test
    fun `empty collection has zero cards`() =
        runTest(testDispatcher) {
            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(state.collection.isEmpty())
            assertEquals(0, state.cardsCount)
            assertFalse(state.isLoadingCards)
            assertNull(state.cardsError)
        }

    @Test
    fun `collection error does not remove profile information`() =
        runTest(testDispatcher) {
            coEvery {
                getMyCardsUseCase.invoke()
            } throws IOException("Network unavailable")

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals("testuser", state.username)
            assertEquals("test@example.com", state.email)
            assertEquals("Mi colección", state.description)

            assertEquals(
                ProfileCardsError.LOAD_FAILED,
                state.cardsError
            )

            assertFalse(state.isLoadingCards)
        }

    @Test
    fun `retry after failure loads cards successfully`() =
        runTest(testDispatcher) {
            coEvery {
                getMyCardsUseCase.invoke()
            } throws IOException("Network unavailable") andThen
                    listOf(card("card-1"))

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                ProfileCardsError.LOAD_FAILED,
                viewModel.uiState.value.cardsError
            )

            viewModel.loadMyCards()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(1, state.cardsCount)
            assertEquals(1, state.collection.size)
            assertNull(state.cardsError)
            assertFalse(state.isLoadingCards)

            coVerify(exactly = 2) {
                getMyCardsUseCase.invoke()
            }
        }

    @Test
    fun `simultaneous collection loads only call use case once`() =
        runTest(testDispatcher) {
            val gate = CompletableDeferred<Unit>()

            coEvery {
                getMyCardsUseCase.invoke()
            } coAnswers {
                gate.await()
                listOf(card("card-1"))
            }

            val viewModel = createViewModel()

            runCurrent()

            assertTrue(viewModel.uiState.value.isLoadingCards)

            viewModel.loadMyCards()
            runCurrent()

            coVerify(exactly = 1) {
                getMyCardsUseCase.invoke()
            }

            gate.complete(Unit)
            advanceUntilIdle()

            assertEquals(1, viewModel.uiState.value.cardsCount)
            assertFalse(viewModel.uiState.value.isLoadingCards)

            coVerify(exactly = 1) {
                getMyCardsUseCase.invoke()
            }
        }

    @Test
    fun `profile updates do not overwrite collection`() =
        runTest(testDispatcher) {
            coEvery {
                getMyCardsUseCase.invoke()
            } returns listOf(card("card-1"))

            val viewModel = createViewModel()
            advanceUntilIdle()

            profileFlow.value = testProfile.copy(
                username = "updateduser"
            )

            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals("updateduser", state.username)
            assertEquals(1, state.cardsCount)
            assertEquals("card-1", state.collection.first().id)
        }

    @Test
    fun `invalid image reference maps to null URL`() =
        runTest(testDispatcher) {
            coEvery {
                getMyCardsUseCase.invoke()
            } returns listOf(
                card("card-1", "invalid-image.jpg")
            )

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertNull(
                viewModel.uiState.value.collection.first().imageUrl
            )
        }

    @Test
    fun `magic card with Cloudinary image is displayed correctly`() =
        runTest(testDispatcher) {
            val magicCard = card(
                id = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee",
                category = CardCategory.MAGIC
            )

            coEvery {
                getMyCardsUseCase.invoke()
            } returns listOf(magicCard)

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(1, state.cardsCount)
            assertEquals(
                magicCard.id,
                state.collection.first().id
            )
            assertEquals(
                magicCard.name,
                state.collection.first().name
            )
            assertEquals(
                magicCard.image,
                state.collection.first().imageUrl
            )
            assertNull(state.cardsError)
        }

    @Test
    fun `cards in auction are still displayed in profile`() =
        runTest(testDispatcher) {
            val availableCard = card(
                id = "card-1",
                inAuction = false
            )

            val cardInAuction = card(
                id = "card-2",
                inAuction = true
            )

            coEvery {
                getMyCardsUseCase.invoke()
            } returns listOf(
                availableCard,
                cardInAuction
            )

            val viewModel = createViewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(2, state.cardsCount)
            assertEquals(2, state.collection.size)

            assertEquals(
                "card-1",
                state.collection[0].id
            )

            assertEquals(
                "card-2",
                state.collection[1].id
            )

            assertNull(state.cardsError)
            assertFalse(state.isLoadingCards)
        }

}