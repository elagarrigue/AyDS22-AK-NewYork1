package ayds.newyork.songinfo.moredetails.model.repository.local

import ayds.newyork.songinfo.moredetails.model.entities.Info

interface NYLocalStorage {

    fun saveArtist(artistName: String, article: Info)

    fun getArtistInfo(artistName: String): Info
}