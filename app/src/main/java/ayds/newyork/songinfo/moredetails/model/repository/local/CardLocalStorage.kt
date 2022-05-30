package ayds.newyork.songinfo.moredetails.model.repository.local

import ayds.newyork.songinfo.moredetails.model.entities.FullCard

interface CardLocalStorage {

    fun saveCard(artistName: String, card: FullCard)

    fun getCard(artistName: String): FullCard?
}