package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaCardService

private const val WIKIPEDIA_LOGO_URL =
    "upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

class ProxyWikipedia(
    private val wikiService: WikipediaCardService
) : Proxy {

    private val dummyCard = FullCard(
        "it.description",
        "article.infoURL",
        "Un gran artista",
        InfoSource.Wikipedia,
        WIKIPEDIA_LOGO_URL
    )

    override fun getCard(artistName: String): Card {
        val artistInfo = wikiService.getCard(artistName)
        return getCardFromService(artistInfo)
    }

    private fun getCardFromService(article: WikipediaArtistInfo?): Card =
        when {
            article != null -> {
                dummyCard
            }
            else -> {
                EmptyCard
            }
        }
}