package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYInfoService {

    fun getArtistInfo(artistName: String): ArtistInfo?
}