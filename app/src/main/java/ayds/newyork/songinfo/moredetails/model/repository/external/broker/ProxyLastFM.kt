package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.lisboa2.lastFM.LASTFM_LOGO
import ayds.lisboa2.lastFM.LastFMArtist
import ayds.lisboa2.lastFM.LastFMService
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource

class ProxyLastFM(
    private val lastFMService: LastFMService
) : Proxy {

    override fun getCard(artistName: String): Card {
        val artistInfo = lastFMService.getArtist(artistName)
        return getCardFromService(artistInfo)
    }
    private fun getCardFromService(article: LastFMArtist?): Card =
        article?.let {
            FullCard(
                it.description,
                it.infoURL,
                it.artistName,
                InfoSource.LastFM,
                LASTFM_LOGO
            )
        }.run {
            EmptyCard
        }
}
