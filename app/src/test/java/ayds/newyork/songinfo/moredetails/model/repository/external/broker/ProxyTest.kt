package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.lisboa2.lastFM.LastFMArtist
import ayds.lisboa2.lastFM.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

private const val ARTIST = "artistName"
private const val DESCRIPTION = "description"
private const val INFOURL = "infourl"
private const val LOGOURL = "logoUrl"

class ProxyTest {

    private val timesService: NYInfoService = mockk(relaxed = true)
    private val wikiService: WikipediaService = mockk(relaxed = true)
    private val lastFMService: LastFMService = mockk(relaxed = true)

    private val proxyNYTimes: Proxy = ProxyNewYorkTimes(timesService)
    private val proxyWikipedia: Proxy = ProxyWikipedia(wikiService)
    private val proxyLastFM: Proxy = ProxyLastFM(lastFMService)

    @Test
    fun `given an existing artist it should return a correct FullCard`() {

        val nytArtist = NYArticle(DESCRIPTION, INFOURL, ARTIST, LOGOURL)
        val wikipediaArticle = WikipediaArtistInfo(DESCRIPTION, INFOURL, LOGOURL)
        val lastFMArtist = LastFMArtist(ARTIST, DESCRIPTION, INFOURL)

        every { lastFMService.getArtist(ARTIST) } returns lastFMArtist
        every { wikiService.getArtistInfo(ARTIST) } returns wikipediaArticle
        every { timesService.getArtistInfo(ARTIST) } returns nytArtist

        var card = proxyLastFM.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)

        card = proxyWikipedia.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)

        card = proxyNYTimes.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)

    }

    @Test
    fun `given an non existing artist it should return an EmptyCard`() {

        val artistInfo = null
        every { lastFMService.getArtist(ARTIST) } returns artistInfo
        every { wikiService.getArtistInfo(ARTIST) } returns artistInfo
        every { timesService.getArtistInfo(ARTIST) } returns artistInfo

        var card = proxyLastFM.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)

        card = proxyWikipedia.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)

        card = proxyNYTimes.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)
    }
}