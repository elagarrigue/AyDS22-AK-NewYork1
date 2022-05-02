package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.EmptyInfo
import ayds.newyork.songinfo.moredetails.model.entities.Info
import ayds.newyork.songinfo.moredetails.model.repository.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Info
}

internal class ArticleRepositoryImpl(
    private val nyInfoService: NYInfoService,
    private val nyLocalStorage: NYLocalStorage
) : ArticleRepository {
    override fun getArticleByArtistName(artistName: String): Info {
        var article = nyLocalStorage.getArtistInfo(artistName)
        when (article) {
            is EmptyInfo -> {
                article = nyInfoService.getArtistInfo(artistName)
                nyLocalStorage.saveArtist(article)
            }
        }
        return article
    }
}