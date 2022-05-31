package ayds.newyork.songinfo.moredetails.model.repository

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

private const val ARTIST_NAME = "artistName"

class CardRepositoryTest {

    private val nyInfoService: NYInfoService = mockk(relaxUnitFun = true)
    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(nyInfoService, cardLocalStorage)
    }

    @Test
    fun `given non existing article should return empty Article`() {
        every { cardLocalStorage.getCard(ARTIST_NAME) } returns null

        val result = cardRepository.getCardByArtistName(ARTIST_NAME)

        assertEquals(EmptyCard, result)
    }

    @Test
    fun `given existing article should return article and mark it as local`() {
        val article = FullCard("articleInformation", "articleURL", ARTIST_NAME,"Source","sourceLogo")
        every { cardLocalStorage.getCard(ARTIST_NAME) } returns article

        val result = cardRepository.getCardByArtistName(ARTIST_NAME)

        assertEquals(article, result)
        assertTrue(article.isLocallyStored)
    }

    @Test
    fun `given non existing article should get the article and store it`() {
        val articleCard = FullCard("articleInformation", "articleURL",ARTIST_NAME,"Source","sourceLogo")
        val articleCardsExtern = NYArticle("articleInformation", "articleURL",ARTIST_NAME,"Source","sourceLogo")
        every { cardLocalStorage.getCard(ARTIST_NAME) } returns null
        every { nyInfoService.getArtistInfo(ARTIST_NAME) } returns articleCardsExtern

        val result = cardRepository.getCardByArtistName(ARTIST_NAME)

        assertEquals(articleCard, result)
        assertFalse(articleCard.isLocallyStored)
        verify { cardLocalStorage.saveCard(ARTIST_NAME, articleCard) }
    }


    @Test
    fun `given service exception should return empty article`() {
        every { cardLocalStorage.getCard(ARTIST_NAME) } returns null
        every { nyInfoService.getArtistInfo(ARTIST_NAME) } throws mockk<Exception>()

        val result = cardRepository.getCardByArtistName(ARTIST_NAME)

        assertEquals(EmptyCard, result)
    }
}