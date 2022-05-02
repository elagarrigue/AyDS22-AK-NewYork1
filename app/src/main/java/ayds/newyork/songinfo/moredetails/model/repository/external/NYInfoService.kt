package ayds.newyork.songinfo.moredetails.model.repository.external

interface NYInfoService {

    fun getArtistInfo(artistName: String): String?
}