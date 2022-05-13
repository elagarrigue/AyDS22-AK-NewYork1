package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.controller.MoreDetailsControllerInjector
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector

object MoreDetailsViewInjector {
    val articleDescriptionHelper: ArticleDescriptionHelper = ArticleDescriptionHelperImpl()
    fun init(moreDetailsView: MoreDetailsView) {
        MoreDetailsModelInjector.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerInjector.onViewStarted(moreDetailsView)
    }
}