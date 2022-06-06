package ayds.newyork.songinfo.moredetails.model.repository
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.Broker


interface CardRepository {
    fun getCardsByArtistName(artistName: String):List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository {

    override fun getCardsByArtistName(artistName: String): List<Card> {
        val repositoryCards = cardLocalStorage.getCards(artistName)
        var justEmptyCards=false

        for (Card in repositoryCards){
            if (Card!=EmptyCard){
                markCardAsLocal(Card)
                justEmptyCards=true
            }

        }

        when {
            justEmptyCards -> return repositoryCards

            else -> {
                val brokerCardList = broker.getCards(artistName)
                if (brokerCardList.isNotEmpty())
                    for(Card in brokerCardList) {
                        cardLocalStorage.saveCard(artistName,Card)
                    }
                return brokerCardList
            }
        }
    }

    private fun markCardAsLocal(card: Card?) {
        if (card != null) {
            card.isLocallyStored = true
        }
    }
}