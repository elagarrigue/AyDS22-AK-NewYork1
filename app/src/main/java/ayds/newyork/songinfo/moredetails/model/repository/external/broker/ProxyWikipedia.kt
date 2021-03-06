package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaService

private const val LOGO_URL =
    "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

class ProxyWikipedia(
    private val wikiService: WikipediaService
) : Proxy {

    override fun getCard(artistName: String): Card {
        val artistInfo = wikiService.getArtistInfo(artistName)
        return getCardFromService(artistInfo, artistName)
    }

    private fun getCardFromService(article: WikipediaArtistInfo?, artistName: String): Card =
        when {
            article != null -> {
                FullCard(
                    article.description,
                    article.infoURL,
                    artistName,
                    InfoSource.Wikipedia,
                    LOGO_URL
                )
            }
            else -> {
                EmptyCard
            }
        }
}