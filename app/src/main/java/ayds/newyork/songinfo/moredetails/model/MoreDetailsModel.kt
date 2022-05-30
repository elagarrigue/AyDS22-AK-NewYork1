package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val cardObservable: Observable<Card>

    fun searchCard(artistName: String)
}

internal class MoreDetailsModelImpl(private val repository: CardRepository) : MoreDetailsModel {

    override val cardObservable = Subject<Card>()

    override fun searchCard(artistName: String) {
        val article = repository.getCardByArtistName(artistName)
        cardObservable.notify(article)
    }
}