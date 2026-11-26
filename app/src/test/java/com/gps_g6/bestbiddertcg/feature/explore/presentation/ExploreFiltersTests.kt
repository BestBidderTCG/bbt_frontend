package com.gps_g6.bestbiddertcg.feature.explore.presentation

import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSort
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionStatus
import com.gps_g6.bestbiddertcg.feature.auction.domain.model.AuctionSummary
import com.gps_g6.bestbiddertcg.feature.auction.domain.repository.AuctionRepository
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCategory
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCollection
import com.gps_g6.bestbiddertcg.feature.card.domain.model.CardCondition
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias de los filtros de la pantalla Explore.
 *
 * Comprueba que los filtros de subastas funcionan correctamente.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreFiltersTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AuctionRepository
    private lateinit var viewModel: ExploreViewModel

    /**
     * Configura el entorno de prueba.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    /**
     * Limpia el entorno de prueba.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprueba que se muestran únicamente las subastas activas.
     */
    @Test
    fun `active filter shows only active auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(active = true)

        assertEquals(
            listOf("Charizard", "Black Lotus", "Mewtwo"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que se muestran únicamente las subastas próximas.
     */
    @Test
    fun `upcoming filter shows only upcoming auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(active = false)

        assertEquals(
            listOf("Pikachu"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que al quitar el filtro de estado se muestran todas las subastas.
     */
    @Test
    fun `clearing status filter shows all auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(active = true)
        viewModel.applyFilters(active = null)

        assertEquals(4, viewModel.auctions.value.size)
    }

    /**
     * Comprueba que el filtro de precio respeta el rango establecido.
     */
    @Test
    fun `price filter shows auctions within range`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(minPrice = 30.0, maxPrice = 60.0)

        assertEquals(
            listOf("Charizard"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que el filtro de condición muestra las cartas correspondientes.
     */
    @Test
    fun `condition filter shows matching auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(
            condition = CardCondition.LIGHT_PLAYED.apiValue
        )

        assertEquals(
            listOf("Pikachu"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que el filtro de colección funciona correctamente.
     */
    @Test
    fun `collection filter shows matching auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(collection = CardCollection.WILDS_OF_ELDRAINE.apiValue)

        assertEquals(
            listOf("Black Lotus"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que el filtro de categoría funciona correctamente.
     */
    @Test
    fun `category filter shows matching auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(category = CardCategory.MAGIC.apiValue)

        assertEquals(
            listOf("Black Lotus"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que el filtro de tiempo restante respeta los días seleccionados.
     */
    @Test
    fun `remaining time filter shows auctions within selected days`() =
        runTest(testDispatcher) {
            prepareAuctions(this)

            viewModel.applyFilters(endingWithinDays = 2)

            assertEquals(
                listOf("Charizard", "Pikachu"),
                viewModel.auctions.value.map { it.cardName }
            )
        }

    /**
     * Comprueba que se pueden combinar varios filtros.
     */
    @Test
    fun `combined filters show matching auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(
            active = true,
            minPrice = 30.0,
            maxPrice = 60.0,
            collection = CardCollection.POKEMON_151.apiValue
        )

        assertEquals(
            listOf("Charizard"),
            viewModel.auctions.value.map { it.cardName }
        )
    }

    /**
     * Comprueba que al limpiar los filtros se recuperan todas las subastas.
     */
    @Test
    fun `clear filters restores all auctions`() = runTest(testDispatcher) {
        prepareAuctions(this)

        viewModel.applyFilters(
            active = true,
            minPrice = 40.0,
            query = "Charizard"
        )

        assertEquals(1, viewModel.auctions.value.size)

        viewModel.clearFilters()
        advanceUntilIdle()

        assertEquals(4, viewModel.auctions.value.size)
    }

    /**
     * Prepara las subastas utilizadas en las pruebas.
     */
    private fun prepareAuctions(testScope: TestScope) {
        val auctions = listOf(
            createAuction(
                "1", "Charizard", 50.0, 1, AuctionStatus.ACTIVE,
                collection = CardCollection.POKEMON_151,
                condition = CardCondition.NEW,
                category = CardCategory.POKEMON
            ),
            createAuction(
                "2", "Pikachu", 20.0, 2, AuctionStatus.UPCOMING,
                collection = CardCollection.DESTINOS_DE_PALDEA,
                condition = CardCondition.LIGHT_PLAYED,
                category = CardCategory.POKEMON
            ),
            createAuction(
                "3", "Black Lotus", 100.0, 5, AuctionStatus.ACTIVE,
                collection = CardCollection.WILDS_OF_ELDRAINE,
                condition = CardCondition.NEW,
                category = CardCategory.MAGIC
            ),
            createAuction(
                "4", "Mewtwo", 80.0, 3, AuctionStatus.ACTIVE,
                collection = CardCollection.SURGING_SPARKS,
                condition = CardCondition.NEW,
                category = CardCategory.POKEMON
            )
        )

        coEvery {
            repository.getAllAuctions(AuctionSort.ENDING_SOON)
        } returns auctions

        viewModel = ExploreViewModel(repository)
        testScope.advanceUntilIdle()
    }

    /**
     * Crea una subasta de prueba.
     */
    private fun createAuction(
        id: String,
        name: String,
        price: Double,
        days: Int,
        status: AuctionStatus,
        category: CardCategory,
        collection: CardCollection,
        condition: CardCondition
    ): AuctionSummary {
        return AuctionSummary(
            id = id,
            cardName = name,
            cardImage = "",
            currentBid = price,
            remainingDays = days,
            cardState = condition,
            cardCategory = category,
            cardCollection = collection,
            cardNumber = null,
            status = status
        )
    }
}