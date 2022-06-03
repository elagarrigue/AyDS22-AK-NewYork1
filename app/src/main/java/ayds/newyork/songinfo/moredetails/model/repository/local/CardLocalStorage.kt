package ayds.newyork.songinfo.moredetails.model.repository.local

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface CardLocalStorage {

    fun saveCard(artistName: String, card: Card)

    fun getCards(artistName: String): List<Card>
}