package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.Auction
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.CreateAuction
import com.gps_g6.bestbiddertcg.feature.auction.domain.usecase.CreateAuctionUseCase
import com.gps_g6.bestbiddertcg.feature.card.domain.model.Card
import com.gps_g6.bestbiddertcg.feature.card.domain.usecase.GetMyCardsUseCase
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * Pruebas unitarias del ViewModel de creación de subastas.
 *
 * Comprueba las validaciones del formulario, la preparación de los datos
 * y el resultado de la creación de una subasta.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreateAuctionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var createAuctionUseCase: CreateAuctionUseCase
    private lateinit var getMyCardsUseCase: GetMyCardsUseCase
    private lateinit var profileManager: ProfileManager
    private lateinit var viewModel: CreateAuctionViewModel

    private val testCard = Card(
        id = "card-1",
        ownerId = "user-1",
        name = "Pikachu",
        image = "",
        category = "Pokemon",
        collection = "Base Set",
        num = 25,
        state = "NM",
        description = null
    )

    private val testProfile = Profile(
        id = "user-1",
        username = "testuser",
        email = "test@example.com",
        description = null,
        avatarNum = 1,
        createdAt = "2026-11-05T12:00:00"
    )

    private val testAuction = Auction(
        id = "auction-1",
        creatorId = "user-1",
        creatorUsername = "testuser",
        card = "Pikachu",
        collection = "Base Set",
        minPrize = 10.0,
        startedAt = "2026-11-16T10:15:00Z",
        durationInDays = 3,
        endsAt = "2026-11-19T10:15:00Z",
        isCancelled = false,
        status = "UPCOMING"
    )

    /**
     * Configura el entorno de prueba.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        createAuctionUseCase = mockk()
        getMyCardsUseCase = mockk()
        profileManager = mockk()

        viewModel = CreateAuctionViewModel(
            createAuctionUseCase,
            getMyCardsUseCase,
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
     * Comprueba que los campos del formulario se actualizan correctamente.
     */
    @Test
    fun `form fields are updated correctly`() {
        val date = LocalDate.now().plusDays(1)

        viewModel.updateStartingPrice("12a,50€")
        viewModel.updateDescription("Test auction")
        viewModel.updateDuration(5)
        viewModel.updateStartDateTime(
            date,
            LocalTime.of(10, 7)
        )

        assertEquals("12,50", viewModel.uiState.value.startingPrice)
        assertEquals("Test auction", viewModel.uiState.value.description)
        assertEquals(5, viewModel.uiState.value.durationInDays)
        assertEquals(
            LocalDateTime.of(date, LocalTime.of(10, 15)),
            viewModel.uiState.value.startDateTime
        )
    }

    /**
     * Comprueba que no se permite crear una subasta sin seleccionar una carta.
     */
    @Test
    fun `auction creation without selected card is rejected`() {
        viewModel.createAuction()

        assertEquals(
            "Selecciona una carta.",
            viewModel.uiState.value.errorMessage
        )

        assertFalse(viewModel.uiState.value.isCreating)
    }

    /**
     * Comprueba que no se permite crear una subasta con un precio inválido.
     */
    @Test
    fun `auction creation with invalid price is rejected`() {
        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("0")

        viewModel.createAuction()

        assertEquals(
            "Introduce un precio inicial válido.",
            viewModel.uiState.value.errorMessage
        )

        assertFalse(viewModel.uiState.value.isCreating)
    }

    /**
     * Comprueba que no se permite crear una subasta con una fecha pasada.
     */
    @Test
    fun `auction creation with past date is rejected`() {
        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("10")
        viewModel.updateStartDateTime(
            LocalDate.now().minusDays(1),
            LocalTime.NOON
        )

        viewModel.createAuction()

        assertEquals(
            "La fecha de inicio ya ha pasado. Elige otra.",
            viewModel.uiState.value.errorMessage
        )

        assertFalse(viewModel.uiState.value.isCreating)
    }

    /**
     * Comprueba que una subasta se crea correctamente con los datos del formulario.
     */
    @Test
    fun `auction is created successfully`() = runTest(testDispatcher) {
        val date = LocalDate.now().plusDays(1)

        val expectedRequest = CreateAuction(
            creatorId = "user-1",
            creatorUsername = "testuser",
            cardId = "card-1",
            description = "Test auction",
            minPrize = 12.5,
            startedAt = LocalDateTime.of(
                date,
                LocalTime.of(10, 15)
            ).atZone(ZoneId.systemDefault()).toInstant().toString(),
            durationInDays = 5
        )

        coEvery {
            profileManager.loadProfile()
        } returns testProfile

        coEvery {
            createAuctionUseCase(expectedRequest)
        } returns testAuction

        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("12,50")
        viewModel.updateDescription(" Test auction ")
        viewModel.updateDuration(5)
        viewModel.updateStartDateTime(
            date,
            LocalTime.of(10, 7)
        )

        viewModel.createAuction()
        advanceUntilIdle()

        assertEquals(
            testAuction,
            viewModel.uiState.value.createdAuction
        )
        assertFalse(viewModel.uiState.value.isCreating)
        assertNull(viewModel.uiState.value.errorMessage)

        coVerify(exactly = 1) {
            profileManager.loadProfile()
        }

        coVerify(exactly = 1) {
            createAuctionUseCase(expectedRequest)
        }
    }

    /**
     * Comprueba que se gestiona un error durante la creación de la subasta.
     */
    @Test
    fun `auction creation error is handled correctly`() = runTest(testDispatcher) {
        coEvery {
            profileManager.loadProfile()
        } returns testProfile

        coEvery {
            createAuctionUseCase(any())
        } throws RuntimeException("Connection error")

        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("10")

        viewModel.createAuction()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCreating)
        assertNull(viewModel.uiState.value.createdAuction)
        assertEquals(
            "No se ha podido crear la subasta.",
            viewModel.uiState.value.errorMessage
        )

        coVerify(exactly = 1) {
            createAuctionUseCase(any())
        }
    }


    /**
     * Comprueba que se rechaza un precio con formato decimal incorrecto.
     */
    @Test
    fun `auction creation with malformed price is rejected`() {
        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("10,50,30")

        viewModel.createAuction()

        assertEquals(
            "Introduce un precio inicial válido.",
            viewModel.uiState.value.errorMessage
        )
        assertFalse(viewModel.uiState.value.isCreating)
    }


    /**
     * Comprueba que la duración solo acepta valores entre 1 y 7 días.
     */
    @Test
    fun `invalid auction duration is ignored`() {
        viewModel.updateDuration(10)

        assertEquals(
            3,
            viewModel.uiState.value.durationInDays
        )
    }

    /**
     * Comprueba que no se permite crear una subasta sin precio inicial.
     */
    @Test
    fun `auction creation without price is rejected`() {
        viewModel.selectCard(testCard)
        viewModel.updateStartingPrice("")

        viewModel.createAuction()

        assertEquals(
            "Introduce un precio inicial válido.",
            viewModel.uiState.value.errorMessage
        )
        assertFalse(viewModel.uiState.value.isCreating)
    }

}
