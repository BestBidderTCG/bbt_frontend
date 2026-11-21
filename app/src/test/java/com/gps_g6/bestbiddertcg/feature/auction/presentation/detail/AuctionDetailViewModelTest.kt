package com.gps_g6.bestbiddertcg.feature.auction.presentation.detail

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionDetail
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.GetAuctionDetailUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import com.gps_g6.bestbiddertcg.feature.profile.domain.model.Profile
import com.gps_g6.bestbiddertcg.feature.profile.session.ProfileManager
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
import java.time.Instant

/**
 * Pruebas unitarias del ViewModel de detalle de subasta.
 *
 * Comprueba la carga de los datos, la identificación del propietario
 * y la gestión de errores y estados de la subasta.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuctionDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAuctionDetailUseCase: GetAuctionDetailUseCase
    private lateinit var profileManager: ProfileManager
    private lateinit var viewModel: AuctionDetailViewModel

    private val testProfile = Profile(
        id = "user-1",
        username = "testuser",
        email = "test@example.com",
        description = null,
        avatarNum = 1,
        createdAt = "2026-11-05T12:00:00"
    )

    private fun createAuctionDetail(
        status: AuctionStatus = AuctionStatus.UPCOMING,
        creatorId: String? = "user-2"
    ): AuctionDetail {
        val now = Instant.now()

        return AuctionDetail(
            id = "auction-1",
            status = status,
            cardName = "Pikachu",
            cardImage = "uploads/pikachu.jpg",
            cardCategory = CardCategory.POKEMON,
            cardCollection = CardCollection.POKEMON_151,
            cardNumber = 25,
            cardState = CardCondition.NEAR_MINT,
            minPrize = 10.0,
            increment = 0.5,
            lastBid = 12.0,
            bidCount = 3,
            startedAt = now.plusSeconds(3600).toEpochMilli(),
            endsAt = now.plusSeconds(86400).toEpochMilli(),
            description = "Test auction",
            creatorId = creatorId,
            creatorUsername = "seller",
            creatorAvatarNum = 2,
            isFavorite = true
        )
    }

    /**
     * Configura el entorno de prueba.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getAuctionDetailUseCase = mockk()
        profileManager = mockk()

        viewModel = AuctionDetailViewModel(
            getAuctionDetailUseCase,
            profileManager
        )
    }

    /**
     * Limpia el entorno de prueba.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprueba que los datos de una subasta se cargan correctamente.
     */
    @Test
    fun `auction detail is loaded successfully`() = runTest(testDispatcher) {
        val auctionDetail = createAuctionDetail()

        coEvery {
            getAuctionDetailUseCase("auction-1")
        } returns auctionDetail

        coEvery {
            profileManager.loadProfile()
        } returns testProfile

        viewModel.loadAuction("auction-1")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals("auction-1", state.auctionId)
        assertEquals("Pikachu", state.cardName)
        assertEquals("uploads/pikachu.jpg", state.cardImage)
        assertEquals(CardCollection.POKEMON_151, state.cardCollection)
        assertEquals(CardCategory.POKEMON, state.cardCategory)
        assertEquals(CardCondition.NEAR_MINT, state.cardCondition)
        assertEquals("25", state.cardNumber)
        assertEquals(12.0, state.currentBid, 0.0)
        assertEquals(10.0, state.startingPrice, 0.0)
        assertEquals(3, state.bidCount)
        assertEquals(0.5, state.minimumIncrement, 0.0)
        assertEquals("Test auction", state.description)
        assertEquals("seller", state.sellerName)
        assertEquals(2, state.sellerAvatar)
        assertTrue(state.isFavorite)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

        coVerify(exactly = 1) {
            getAuctionDetailUseCase("auction-1")
        }
    }

    /**
     * Comprueba que no se carga una subasta con un identificador vacío.
     */
    @Test
    fun `auction detail with empty id is rejected`() {
        viewModel.loadAuction("")

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(
            "El identificador de la subasta no es válido.",
            state.errorMessage
        )

        coVerify(exactly = 0) {
            getAuctionDetailUseCase(any())
        }
    }

    /**
     * Comprueba que se gestiona un error al cargar la subasta.
     */
    @Test
    fun `auction detail loading error is handled correctly`() =
        runTest(testDispatcher) {

            coEvery {
                getAuctionDetailUseCase("auction-1")
            } throws RuntimeException("Connection error")

            viewModel.loadAuction("auction-1")
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertFalse(state.isLoading)
            assertEquals(
                "No se ha podido cargar el detalle de la subasta.",
                state.errorMessage
            )

            coVerify(exactly = 1) {
                getAuctionDetailUseCase("auction-1")
            }
        }

    /**
     * Comprueba que una subasta propia se identifica correctamente.
     */
    @Test
    fun `own auction is identified correctly`() = runTest(testDispatcher) {
        val auctionDetail = createAuctionDetail(creatorId = "user-1")

        coEvery {
            getAuctionDetailUseCase("auction-1")
        } returns auctionDetail

        coEvery {
            profileManager.loadProfile()
        } returns testProfile

        viewModel.loadAuction("auction-1")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isOwner)
    }

    /**
     * Un creador no identificado no se considera propietario.
     */
    @Test
    fun `auction with unknown creator is not identified as own`() = runTest(testDispatcher) {
        coEvery { getAuctionDetailUseCase("auction-1") } returns
                createAuctionDetail(creatorId = null)
        coEvery { profileManager.loadProfile() } returns testProfile

        viewModel.loadAuction("auction-1")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isOwner)
    }

    /**
     * Comprueba que una subasta de otro usuario no se identifica como propia.
     */
    @Test
    fun `another user auction is identified correctly`() =
        runTest(testDispatcher) {

            val auctionDetail = createAuctionDetail(
                creatorId = "user-2"
            )

            coEvery {
                getAuctionDetailUseCase("auction-1")
            } returns auctionDetail

            coEvery {
                profileManager.loadProfile()
            } returns testProfile

            viewModel.loadAuction("auction-1")
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertFalse(state.isOwner)
            assertEquals("seller", state.sellerName)
        }

    /**
     * Comprueba que una subasta próxima se representa correctamente.
     */
    @Test
    fun `upcoming auction status is mapped correctly`() =
        runTest(testDispatcher) {

            val auctionDetail = createAuctionDetail(
                status = AuctionStatus.UPCOMING
            )

            coEvery {
                getAuctionDetailUseCase("auction-1")
            } returns auctionDetail

            coEvery {
                profileManager.loadProfile()
            } returns testProfile

            viewModel.loadAuction("auction-1")
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(
                AuctionStatus.UPCOMING,
                state.status
            )
            assertTrue(state.remainingTime.isNotBlank())
            assertTrue(state.scheduledStart.isNotBlank())
        }

    /**
     * Comprueba que una subasta activa se representa correctamente.
     */
    @Test
    fun `active auction status is mapped correctly`() =
        runTest(testDispatcher) {

            val auctionDetail = createAuctionDetail(
                status = AuctionStatus.ACTIVE
            )

            coEvery {
                getAuctionDetailUseCase("auction-1")
            } returns auctionDetail

            coEvery {
                profileManager.loadProfile()
            } returns testProfile

            viewModel.loadAuction("auction-1")
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(
                AuctionStatus.ACTIVE,
                state.status
            )
            assertTrue(state.remainingTime.isNotBlank())
        }
}