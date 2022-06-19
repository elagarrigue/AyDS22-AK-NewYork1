package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.lisboa2.lastFM.LastFMArtist
import ayds.lisboa2.lastFM.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class ProxyLastFmTest {
    private val lastFMService: LastFMService = mockk(relaxed = true)
    private val proxyLastFM: Proxy = ProxyLastFM(lastFMService)

    @Test
    fun `given an existing artist it should return a correct FullCard`() {

        val lastFMArtist = LastFMArtist(ARTIST, DESCRIPTION, INFOURL)
        every { lastFMService.getArtist(ARTIST) } returns lastFMArtist

        val card = proxyLastFM.getCard(ARTIST)
        Assert.assertTrue(card is FullCard)
        Assert.assertTrue(card.sameCards(lastFMArtist))

        verify { lastFMService.getArtist(ARTIST) }
    }

    private fun Card.sameCards(lastFMArtist: LastFMArtist) =
        this.artistName == lastFMArtist.artistName &&
                this.description == lastFMArtist.description &&
                this.infoURL == lastFMArtist.infoURL

    @Test
    fun `given an non existing artist it should return an EmptyCard`() {

        val artistInfo = null
        every { lastFMService.getArtist(ARTIST) } returns artistInfo

        val card = proxyLastFM.getCard(ARTIST)
        Assert.assertTrue(card is EmptyCard)
        verify { lastFMService.getArtist(ARTIST) }
    }
}