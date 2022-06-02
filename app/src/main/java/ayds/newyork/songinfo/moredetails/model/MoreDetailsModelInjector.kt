package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import ayds.newyork.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.ak1.newyorktimes.article.external.NYInjector
import ayds.lisboa2.lastFM.LastFMInjector
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.*
import ayds.newyork.songinfo.moredetails.model.repository.external.broker.BrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.CardLocalStorageImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.CursorToCardMapperImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.winchester1.wikipedia.WikipediaInjector

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {

        val cardLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToCardMapperImpl()
        )

        val proxyNYTimes: Proxy = ProxyNewYorkTimes(NYInjector.nyInfoService)
        val proxyWikipedia: Proxy = ProxyWikipedia(WikipediaInjector.wikipediaCardService)
        val proxyLastFM: Proxy = ProxyLastFM(LastFMInjector.lastFMService)
        val cardBroker: Broker = BrokerImpl(listOf(proxyLastFM, proxyWikipedia, proxyNYTimes))

        val repository: CardRepository =
            CardRepositoryImpl(cardLocalStorage, cardBroker)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}