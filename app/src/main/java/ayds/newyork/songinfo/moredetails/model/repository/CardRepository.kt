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
        val existFullCards = checkListForFullCards(repositoryCards)

        return when {
            existFullCards -> repositoryCards
            else -> {
                val brokerCardList = broker.getCards(artistName)
                    for(Card in brokerCardList) {
                        if (Card!=EmptyCard) {
                            cardLocalStorage.saveCard(artistName,Card)
                        }
                    }
                brokerCardList
            }
        }
    }

    private fun checkListForFullCards(repositoryCards:List<Card>):Boolean {
        var existFullCards=false
        for (Card in repositoryCards){
            if (Card!=EmptyCard){
                markCardAsLocal(Card)
                existFullCards=true
            }
        }
        return existFullCards
    }

    private fun markCardAsLocal(card: Card) {
        card.isLocallyStored = true
    }
}