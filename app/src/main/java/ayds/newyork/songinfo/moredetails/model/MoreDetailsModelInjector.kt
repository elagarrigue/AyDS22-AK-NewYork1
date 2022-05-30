package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.newyork.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.ak1.newyorktimes.article.external.NYInjector
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.CardLocalStorageImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.CursorToCardMapperImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {

        val cardLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToCardMapperImpl()
        )

        val nyInfoService: NYInfoService = NYInjector.nyInfoService

        val repository: CardRepository =
            CardRepositoryImpl(nyInfoService, cardLocalStorage)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}