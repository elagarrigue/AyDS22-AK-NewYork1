package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.FullCard

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerImpl(
    private val proxies: List<Proxy>
) : Broker {
    override fun getCards(artistName: String): List<Card> =
        mutableListOf<Card>().apply {
            for (proxy in proxies) {
                val card = proxy.getCard(artistName)
                if (card is FullCard)
                    add(card)
            }
        }
}