package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource

class ProxyNewYorkTimes(
    private val NYTimesService: NYInfoService
) : Proxy {
    override fun getCard(artistName: String): Card {
        val infoArtist = NYTimesService.getArtistInfo(artistName)
        return getCardFromService(infoArtist)
    }

    private fun getCardFromService(article: NYArticle?): Card =
        article?.let {
            FullCard(
                it.description,
                it.infoURL,
                it.artistName,
                InfoSource.NewYorkTimes,
                it.logoURL
            )
        }.run {
            EmptyCard
        }

}