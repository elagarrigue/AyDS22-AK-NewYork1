package ayds.newyork.songinfo.moredetails.model.repository.local

interface NYLocalStorage {

    fun saveArtist(artist: String, info: String)

    fun getArtistInfo(artistName: String): String?
}