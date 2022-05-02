package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Info

interface NYInfoService {

    fun getArtistInfo(artistName: String): Info
}