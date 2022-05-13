package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.NYArticle

interface NYInfoService {

    fun getArtistInfo(artistName: String): NYArticle?
}