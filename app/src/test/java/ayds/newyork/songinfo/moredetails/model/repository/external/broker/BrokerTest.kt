package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYInjector
import ayds.lisboa2.lastFM.LastFMInjector
import ayds.winchester1.wikipedia.WikipediaInjector
import org.junit.Assert
import org.junit.Test

class BrokerTest {

    private val proxyNYTimes: Proxy = ProxyNewYorkTimes(NYInjector.nyInfoService)
    private val proxyWikipedia: Proxy = ProxyWikipedia(WikipediaInjector.wikipediaService)
    private val proxyLastFM: Proxy = ProxyLastFM(LastFMInjector.lastFMService)

    private val broker: Broker by lazy {
        BrokerImpl(
            listOf(
                proxyNYTimes,
                proxyWikipedia,
                proxyLastFM
            )
        )
    }
    private val artistName = "Cerati"

    @Test
    fun `given an artist name it should return a 3 elements list`() {
        val cardsList = broker.getCards(artistName)
        Assert.assertTrue(cardsList.size == 3)
    }

}