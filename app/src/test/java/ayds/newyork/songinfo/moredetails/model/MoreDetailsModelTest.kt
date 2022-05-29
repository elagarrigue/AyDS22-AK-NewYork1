package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Article
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
        val article: Article = mockk()
        every { repository.getArticleByArtistName("name")} returns article
        val articleTester: (Article) -> Unit = mockk(relaxed = true)
        moreDetailsModel.articleObservable.subscribe {
            articleTester(it)
        }

        moreDetailsModel.getInfoArticle("name")

        verify { articleTester(article) }
    }

}