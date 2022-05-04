package ayds.newyork.songinfo.moredetails.model.repository.local

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYLocalStorage {

    fun saveArtist(artistName: String, article: ArtistInfo)

    fun getArtistInfo(artistName: String): ArtistInfo?
}