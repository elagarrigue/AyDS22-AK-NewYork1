package ayds.newyork.songinfo.moredetails.model.repository

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.ak1.newyorktimes.article.external.NYInfoService


interface CardRepository {
    fun getCardByArtistName(artistName: String): Card
}

internal class CardRepositoryImpl(
    private val nyInfoService: NYInfoService,
    private val cardLocalStorage: CardLocalStorage
) : CardRepository {

    override fun getCardByArtistName(artistName: String): Card {
        var card = cardLocalStorage.getCard(artistName)
        when {
            card != null -> markArticleAsLocal(card)
            else -> {
                try {
                    val nyArtistInfo = nyInfoService.getArtistInfo(artistName)
                    nyArtistInfo?.let {
                        card = createNYInfoCard(artistName, it)
                    }
                    card?.let {
                        cardLocalStorage.saveCard(artistName, it)
                    }
                } catch (e: Exception) {
                    card = null
                }
            }
        }
        return card ?: EmptyCard
    }

    private fun createNYInfoCard(artistName: String, nyArticle: NYArticle): FullCard {
        return FullCard(
            nyArticle.description,
            nyArticle.infoURL,
            artistName,
            nyArticle.logoURL
        )
    }

    private fun markArticleAsLocal(card: FullCard) {
        card.isLocallyStored = true
    }
}