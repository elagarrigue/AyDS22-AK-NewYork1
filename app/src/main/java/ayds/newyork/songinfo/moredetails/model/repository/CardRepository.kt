package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.Broker

interface CardRepository {
    fun getCardsByArtistName(artistName: String): List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getCardsByArtistName(artistName: String): List<Card> {
        val repositoryCards = cardLocalStorage.getCards(artistName)

        when {
            repositoryCards.isNotEmpty() -> for(Card in repositoryCards) {
                markCardAsLocal(Card)
            }
            else -> {
                val brokerCardList = broker.getCards(artistName)
                if (brokerCardList.isNotEmpty())
                    for(Card in brokerCardList) {
                        cardLocalStorage.saveCard(artistName,Card)
                    }
                    return brokerCardList
                }
            }
            return repositoryCards
    }

    private fun markCardAsLocal(card: Card) {
        card.isLocallyStored = true
    }
}