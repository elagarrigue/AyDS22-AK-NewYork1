package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class ProxyNYTimesTest {
    private val timesService: NYInfoService = mockk(relaxed = true)
    private val proxyNYTimes: Proxy = ProxyNewYorkTimes(timesService)

    @Test
    fun `given an existing artist it should return a correct FullCard`() {

        val nytArtist = NYArticle(DESCRIPTION, INFOURL, ARTIST, LOGOURL)
        every { timesService.getArtistInfo(ARTIST) } returns nytArtist

        val card = proxyNYTimes.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)
        Assert.assertTrue(card.sameCards(nytArtist))
        verify { timesService.getArtistInfo(ARTIST) }
    }

    private fun Card.sameCards(nyArticle: NYArticle) =
        this.artistName == nyArticle.artistName &&
        this.description == nyArticle.description &&
        this.infoURL == nyArticle.infoURL &&
        this.sourceLogoURL == nyArticle.logoURL

    @Test
    fun `given an non existing artist it should return an EmptyCard`() {

        val artistInfo = null
        every { timesService.getArtistInfo(ARTIST) } returns artistInfo

        val card = proxyNYTimes.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)
        verify { timesService.getArtistInfo(ARTIST) }
    }
}