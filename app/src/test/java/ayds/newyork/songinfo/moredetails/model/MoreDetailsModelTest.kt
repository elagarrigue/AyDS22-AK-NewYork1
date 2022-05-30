package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.ArticleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: ArticleRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel by lazy {
        MoreDetailsModelImpl(repository)
    }

    @Test
    fun `on search article it should notify the result`() {
        val card: Card = mockk()
        every { repository.getArticleByArtistName("name")} returns card
        val articleTester: (Card) -> Unit = mockk(relaxed = true)
        moreDetailsModel.cardObservable.subscribe {
            articleTester(it)
        }

        moreDetailsModel.getInfoArticle("name")

        verify { articleTester(card) }
    }

}