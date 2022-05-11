package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.ArticleRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArticleRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.external.NYInjector
import ayds.newyork.songinfo.moredetails.model.repository.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.CursorToArticleMapperImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.ArticleLocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {

        val nyLocalStorage: NYLocalStorage = ArticleLocalStorageImpl(
            moreDetailsView as Context, CursorToArticleMapperImpl()
        )

        val nyInfoService: NYInfoService = NYInjector.nyInfoService

        val repository: ArticleRepository =
            ArticleRepositoryImpl(nyInfoService,nyLocalStorage)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}