package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import kotlin.text.Typography.times

class BrokerTest {

    private val proxyNYTimes: ProxyNewYorkTimes = mockk(relaxed = true)
    private val proxyWikipedia: ProxyWikipedia = mockk(relaxed = true)
    private val proxyLastFM: ProxyLastFM = mockk(relaxed = true)

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
        broker.getCards(artistName)
        verify{proxyNYTimes.getCard(artistName)}
        verify{proxyWikipedia.getCard(artistName)}
        verify{proxyLastFM.getCard(artistName)}
    }

}