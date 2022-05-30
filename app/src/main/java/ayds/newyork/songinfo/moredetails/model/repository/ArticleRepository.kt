package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArticle
import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage


interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Article
}

internal class ArticleRepositoryImpl(
    private val nyInfoService: NYInfoService,
    private val nyLocalStorage: NYLocalStorage
) : ArticleRepository {

    override fun getArticleByArtistName(artistName: String): Article {
        var article = nyLocalStorage.getArtistInfo(artistName)

        when {
            article != null -> markArticleAsLocal(article)
            else -> {
                try {
                    article = nyInfoService.getArtistInfo(artistName)

                    article?.let {
                        nyLocalStorage.saveArtistInfo(artistName, it)
                    }
                } catch (e: Exception) {
                    article = null
                }
            }
        }

        return article ?: EmptyArticle
    }

    private fun markArticleAsLocal(article: NYArticle) {
        article.isLocallyStored = true
    }
}