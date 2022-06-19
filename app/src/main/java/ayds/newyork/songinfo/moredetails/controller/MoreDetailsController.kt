package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsUiEvent> =
        Observer { value ->
            when (value) {
                MoreDetailsUiEvent.ShowInfoArticle -> showInfoArticle()
                MoreDetailsUiEvent.OpenMoreInfoUrl -> openMoreInfoUrl()
            }
        }

    private fun openMoreInfoUrl() {
        moreDetailsView.openExternalLink(moreDetailsView.uiState.getCurrentCard().infoURL)
    }

    private fun showInfoArticle() {
        Thread {
            moreDetailsModel.searchCard(moreDetailsView.uiState.artistName)
        }.start()
    }
}