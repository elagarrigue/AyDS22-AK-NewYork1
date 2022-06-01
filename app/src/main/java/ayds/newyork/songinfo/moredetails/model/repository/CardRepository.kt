package ayds.newyork.songinfo.moredetails.model.repository

import ayds.ak1.newyorktimes.article.external.NYArticle
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
// import Broker
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import java.util.*


interface CardRepository {
    fun getCardsByArtistName(artistName: String):List<Card>
}

internal class CardRepositoryImpl(
    //private val informacionBroker: InformacionBrokerTODO Broker
    private val cardLocalStorage: CardLocalStorage
) : CardRepository {

    override fun getCardsByArtistName(artistName: String): List<FullCard> {
        val card = cardLocalStorage.getCards(artistName)
        var brokerCardList= LinkedList<FullCard>()

        when {
            card.isNotEmpty() -> for(Card in card) {
                markArticleAsLocal(Card)
            }
            else -> {
                // TODO call Broker
                // brokerCardList = getList()

                if (brokerCardList.isNotEmpty())
                    for(Card in brokerCardList) {
                        cardLocalStorage.saveCard(artistName,Card)
                    }
            }
        }
        return brokerCardList

    }

    //TODO mover a extra class
    private fun createNYInfoCard(artistName: String, nyArticle: NYArticle): FullCard {
        return FullCard(
            nyArticle.description,
            nyArticle.infoURL,
            artistName,
            InfoSource.NewYorkTimes,
            nyArticle.logoURL
        )
    }

    private fun markArticleAsLocal(card: FullCard?) {
        if (card != null) {
            card.isLocallyStored = true
        }
    }
}