package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.repository.ArticleRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val articleObservable: Observable<Article>

    fun getInfoArticle(artistName: String)
}

internal class MoreDetailsModelImpl(private val repository: ArticleRepository) : MoreDetailsModel {

    override val articleObservable = Subject<Article>()

    override fun getInfoArticle(artistName: String) {
        val article = repository.getArticleByArtistName(artistName)
        articleObservable.notify(article)
    }
}