package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.EmptyArticle
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import ayds.ak1.newyorktimes.article.external.NYInfoService
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

private const val ARTIST_NAME = "artistName"

class ArticleRepositoryTest {

    private val nyInfoService: NYInfoService = mockk(relaxUnitFun = true)
    private val nyLocalStorage: NYLocalStorage = mockk(relaxUnitFun = true)

    private val articleRepository: ArticleRepository by lazy {
        ArticleRepositoryImpl(nyInfoService, nyLocalStorage)
    }

    @Test
    fun `given non existing article should return empty Article`() {
        every { nyLocalStorage.getArtistInfo(ARTIST_NAME) } returns null

        val result = articleRepository.getArticleByArtistName(ARTIST_NAME)

        assertEquals(EmptyArticle, result)
    }

    @Test
    fun `given existing article should return article and mark it as local`() {
        val article = NYArticle("articleInformation", "articleURL", ARTIST_NAME)
        every { nyLocalStorage.getArtistInfo(ARTIST_NAME) } returns article

        val result = articleRepository.getArticleByArtistName(ARTIST_NAME)

        assertEquals(article, result)
        assertTrue(article.isLocallyStored)
    }

    @Test
    fun `given non existing article should get the article and store it`() {
        val article = NYArticle("articleInformation", "articleURL",ARTIST_NAME)
        every { nyLocalStorage.getArtistInfo(ARTIST_NAME) } returns null
        every { nyInfoService.getArtistInfo(ARTIST_NAME) } returns article

        val result = articleRepository.getArticleByArtistName(ARTIST_NAME)

        assertEquals(article, result)
        assertFalse(article.isLocallyStored)
        verify { nyLocalStorage.saveArtistInfo(ARTIST_NAME, article) }
    }


    @Test
    fun `given service exception should return empty article`() {
        every { nyLocalStorage.getArtistInfo(ARTIST_NAME) } returns null
        every { nyInfoService.getArtistInfo(ARTIST_NAME) } throws mockk<Exception>()

        val result = articleRepository.getArticleByArtistName(ARTIST_NAME)

        assertEquals(EmptyArticle, result)
    }
}