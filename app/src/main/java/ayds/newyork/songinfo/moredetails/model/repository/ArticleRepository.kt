package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
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

        when {
            article != null -> markArticleAsLocal(article)
            else -> {
                try {
                    article = nyInfoService.getArtistInfo(artistName)

                    article?.let {
                            nyLocalStorage.saveArtist(artistName, it)
                    }
                } catch (e: Exception) {
                    article = null
                }
            }
        }

        return article ?: EmptyInfo
    }

    private fun markArticleAsLocal(article: ArtistInfo) {
        article.isLocallyStored = true
    }
}