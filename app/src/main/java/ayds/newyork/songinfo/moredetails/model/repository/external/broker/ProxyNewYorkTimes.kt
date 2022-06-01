package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.entities.Card

class ProxyNewYorkTimes(
    private val NYTimesService: NYInfoService
): Proxy {
    override fun getCard(artistName: String): Card? {
        TODO("Not yet implemented")
    }
}