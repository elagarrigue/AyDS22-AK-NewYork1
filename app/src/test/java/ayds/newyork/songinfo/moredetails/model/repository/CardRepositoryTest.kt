package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.Broker
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

private const val ARTIST_NAME = "artistName"
private val EMPTY_CARDS = listOf(EmptyCard, EmptyCard, EmptyCard)

class CardRepositoryTest {

    private val broker: Broker = mockk(relaxUnitFun = true)
    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)


    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, broker)
    }

    @Test
    fun `given non existing card should return List of Empty Card`() {

        every { cardLocalStorage.getCards(ARTIST_NAME) } returns EMPTY_CARDS
        every { broker.getCards(ARTIST_NAME) } returns EMPTY_CARDS

        val result = cardRepository.getCardsByArtistName(ARTIST_NAME)

        assertEquals(EMPTY_CARDS, result)
    }

    @Test
    fun `given existing article should return article and mark it as local`() {
        val cardList = listOf(
            FullCard(
                "articleInformation",
                "articleURL",
                ARTIST_NAME,
                InfoSource.NoSource,
                "sourceLogo"
            )
        )
        every { cardLocalStorage.getCards(ARTIST_NAME) } returns cardList

        val result = cardRepository.getCardsByArtistName(ARTIST_NAME)

        assertEquals(cardList, result)
        for (card in cardList) {
            assertTrue(card.isLocallyStored)
        }
    }

    @Test
    fun `given non existing article should get the article and store it`() {
        val articleCards = listOf(
            FullCard(
                "articleInformation",
                "articleURL",
                ARTIST_NAME,
                InfoSource.NewYorkTimes,
                "sourceLogo"
            )
        )
        every { cardLocalStorage.getCards(ARTIST_NAME) } returns listOf(
            EmptyCard,
            EmptyCard,
            EmptyCard
        )
        every { broker.getCards(ARTIST_NAME) } returns articleCards

        val result = cardRepository.getCardsByArtistName(ARTIST_NAME)

        assertEquals(articleCards, result)
        for (card in articleCards) {
            assertFalse(card.isLocallyStored)
            verify { cardLocalStorage.saveCard(ARTIST_NAME, card) }
        }
    }

}