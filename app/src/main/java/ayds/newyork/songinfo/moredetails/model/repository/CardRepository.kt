package ayds.newyork.songinfo.moredetails.model.repository
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.Broker


interface CardRepository {
    fun getCardsByArtistName(artistName: String):List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getCardsByArtistName(artistName: String): List<Card> {
        val card = cardLocalStorage.getCards(artistName)

        when {
            card.isNotEmpty() -> for(Card in card) {
                markArticleAsLocal(Card)
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
        return card

    }

    private fun markArticleAsLocal(card: Card?) {
        if (card != null) {
            card.isLocallyStored = true
        }
    }
}