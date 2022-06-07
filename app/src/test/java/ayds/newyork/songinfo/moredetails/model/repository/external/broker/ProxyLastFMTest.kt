package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.lisboa2.lastFM.LastFMArtist
import ayds.lisboa2.lastFM.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard

import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ProxyLastFMTest {
    private val lastFMService: LastFMService = mockk(relaxUnitFun = true)
    private val proxyLastFM: Proxy by lazy {
        ProxyLastFM(lastFMService)
    }

    @Test
    fun `on found artist name should return FullCard `() {
        val lastFMArtist = LastFMArtist("artistName", "description", "infoURL")

        every { lastFMService.getArtist("artistName") } returns lastFMArtist
        val card = proxyLastFM.getCard("artistName")

        Assert.assertTrue(card is FullCard)
    }

    @Test
    fun `not found artist name should return EmptyCard `() {
        val lastFMArtist = null
        every { lastFMService.getArtist("artistName") } returns lastFMArtist
        val card = proxyLastFM.getCard("artistName")

        Assert.assertTrue(card is EmptyCard)

    }

}