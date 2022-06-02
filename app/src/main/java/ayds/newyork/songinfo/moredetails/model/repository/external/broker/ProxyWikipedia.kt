package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.lisboa2.lastFM.LASTFM_LOGO
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaCardService

class ProxyWikipedia(
    private val wikiService: WikipediaCardService
): Proxy {
    override fun getCard(artistName: String): Card {
        val artistInfo = wikiService.getCard(artistName)
        return getCardFromService(artistInfo)
    }

    private fun getCardFromService(article: WikipediaArtistInfo?): Card =
        article?.let {
            FullCard(
                it.description,
                it.infoURL,
                "it.artistName", // CAMBIAR
                InfoSource.Wikipedia,
                LASTFM_LOGO // CAMBIAR LINK DEL LOGO
            )
        }.run {
            EmptyCard
        }
}