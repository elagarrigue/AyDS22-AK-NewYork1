package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage


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
                    card = nyInfoService.getArtistInfo(artistName)

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

    private fun markArticleAsLocal(card: FullCard) {
        card.isLocallyStored = true
    }
}