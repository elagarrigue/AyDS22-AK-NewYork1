package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class ProxyWikipediaTest {
    private val wikiService: WikipediaService = mockk(relaxed = true)
    private val proxyWikipedia: Proxy = ProxyWikipedia(wikiService)

    @Test
    fun `given an existing artist it should return a correct FullCard`() {

        val wikipediaArticle = WikipediaArtistInfo(DESCRIPTION, INFOURL, LOGOURL)
        every { wikiService.getArtistInfo(ARTIST) } returns wikipediaArticle

        val card = proxyWikipedia.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)
        Assert.assertTrue(card.sameCards(wikipediaArticle))

        verify { wikiService.getArtistInfo(ARTIST) }
    }

    private fun Card.sameCards(wikipediaArticle: WikipediaArtistInfo) =
        this.description == wikipediaArticle.description &&
                this.infoURL == wikipediaArticle.infoURL &&
                this.sourceLogoURL == wikipediaArticle.sourceLogoURL


    @Test
    fun `given an non existing artist it should return an EmptyCard`() {

        val artistInfo = null
        every { wikiService.getArtistInfo(ARTIST) } returns artistInfo

        val card = proxyWikipedia.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)
        verify { wikiService.getArtistInfo(ARTIST) }
    }
}