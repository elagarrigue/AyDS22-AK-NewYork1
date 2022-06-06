package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: CardRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel by lazy {
        MoreDetailsModelImpl(repository)
    }

    @Test
    fun `on search cards it should notify the result`() {
        val cardList: List<Card> = mockk()
        every { repository.getCardsByArtistName("name")} returns cardList
        val articleTester: (List<Card>) -> Unit = mockk(relaxed = true)
        moreDetailsModel.cardObservable.subscribe {
            articleTester(it)
        }

        moreDetailsModel.searchCard("name")

        verify { articleTester(cardList) }
    }

}