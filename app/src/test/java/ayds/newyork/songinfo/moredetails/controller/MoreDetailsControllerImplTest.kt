package ayds.newyork.songinfo.moredetails.controller


import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiState
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

private const val ARTIST_DESCRIPTION = "desc"
private const val INFO_URL = "url"
private const val ARTIST_NAME = "name"
private const val SOURCE_LOGO = "logo"

class MoreDetailsControllerTest {

    private val moreDetailsModel: MoreDetailsModel = mockk(relaxUnitFun = true)
    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    private val moreDetailsView: MoreDetailsView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val moreDetailsController by lazy {
        MoreDetailsControllerImpl(moreDetailsModel)
    }

    private var cards = mutableListOf<Card>()

    @Before
    fun setup() {
        moreDetailsController.setMoreDetailsView(moreDetailsView)
        for(i in 0..2){
            val card: Card = FullCard(ARTIST_DESCRIPTION,INFO_URL,ARTIST_NAME,InfoSource.NewYorkTimes,SOURCE_LOGO,false)
            cards.add(i,card)
        }
    }

    @Test
    fun `on open more info url event should open external link`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(cardList = cards)

        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)

        verify { moreDetailsView.openExternalLink(INFO_URL) }
    }

    @Test
    fun `on show info article event should search for an Article`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(artistName = ARTIST_NAME)

        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)

        verify { moreDetailsModel.searchCard(ARTIST_NAME) }
    }

}