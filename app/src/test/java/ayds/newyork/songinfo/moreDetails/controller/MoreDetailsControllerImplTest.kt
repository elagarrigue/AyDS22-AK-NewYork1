package ayds.newyork.songinfo.moreDetails.controller

import ayds.newyork.songinfo.moredetails.controller.MoreDetailsControllerImpl
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiState
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsControllerTest {

    private val moreDetailsModel: MoreDetailsModel = mockk(relaxUnitFun = true)
    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    private val moreDetailsView: MoreDetailsView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val moreDetailsController by lazy {
        MoreDetailsControllerImpl(moreDetailsModel)
    }

    @Before
    fun setup() {
        moreDetailsController.setMoreDetailsView(moreDetailsView)
    }

    @Test
    fun `on open more info url event should open external link`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(artistUrl = "url")

        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)

        verify { moreDetailsView.openExternalLink("url") }
    }

    @Test
    fun `on show info article event should search for an Article`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(artistName = "name")

        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)

        verify { moreDetailsModel.getInfoArticle("name") }
    }

}