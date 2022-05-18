package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.EmptyArticle
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import ayds.newyork.songinfo.moredetails.model.repository.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class ArticleRepositoryTest {

    private val nyInfoService: NYInfoService = mockk(relaxUnitFun = true)
    private val nyLocalStorage: NYLocalStorage = mockk(relaxUnitFun = true)

    private val articleRepository: ArticleRepository by lazy {
        ArticleRepositoryImpl(nyInfoService, nyLocalStorage)
    }

    @Test
    fun `given non existing article should return empty Article`() {
        every { nyLocalStorage.getArtistInfo("artistName") } returns null

        val result = articleRepository.getArticleByArtistName("artistName")

        assertEquals(EmptyArticle, result)
    }

    @Test
    fun `given existing article should return article and mark it as local`() {
        val article = NYArticle("articleInformation", "articleURL", 0)
        every { nyLocalStorage.getArtistInfo("artistName") } returns article

        val result = articleRepository.getArticleByArtistName("artistName")

        assertEquals(article, result)
        assertTrue(article.isLocallyStored)
    }

    @Test
    fun `given non existing article should get the article and store it`() {
        val article = NYArticle("articleInformation", "articleURL", 1 )
        every { nyLocalStorage.getArtistInfo("artistName") } returns null
        every { nyInfoService.getArtistInfo("artistName") } returns article

        val result = articleRepository.getArticleByArtistName("artistName")

        assertEquals(article, result)
        assertFalse(article.isLocallyStored)
        verify { nyLocalStorage.saveArtist("artistName", article) }
    }


    @Test
    fun `given service exception should return empty article`() {
        every { nyLocalStorage.getArtistInfo("artistName") } returns null
        every { nyInfoService.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = articleRepository.getArticleByArtistName("artistName")

        assertEquals(EmptyArticle, result)
    }
}