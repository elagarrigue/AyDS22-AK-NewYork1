package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.Broker

interface CardRepository {
    fun getCardsByArtistName(artistName: String): List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getCardsByArtistName(artistName: String): List<Card> {
        var cards = cardLocalStorage.getCards(artistName)
        if (cards.isEmpty()) {
            cards = broker.getCards(artistName)
            if (!anyEmptyCards(cards))
                cards.forEach {
                    cardLocalStorage.saveCard(artistName, it)
                }
        } else
            cards.forEach {
                markCardAsLocal(it)
            }
        return cards
    }

    private fun anyEmptyCards(cards: List<Card>): Boolean =
        cards.any { it is EmptyCard }

    private fun markCardAsLocal(card: Card) {
        card.isLocallyStored = true
    }
}