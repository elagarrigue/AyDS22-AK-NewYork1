package ayds.newyork.songinfo.moredetails.model.repository.local

import ayds.newyork.songinfo.moredetails.model.entities.NYArticle

interface NYLocalStorage {

    fun saveArtistInfo(artistName: String, article: NYArticle)

    fun getArtistInfo(artistName: String): NYArticle?
}