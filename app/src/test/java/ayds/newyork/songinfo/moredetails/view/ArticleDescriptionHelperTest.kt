package ayds.newyork.songinfo.moredetails.view

import android.text.SpannableString
import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ArticleDescriptionHelperTest {
    private val articleDescriptionHelper by lazy { ArticleDescriptionHelperImpl() }

    @Test
    fun `given a local article it should return the description`() {

        val article: Article = NYArticle(
            "reactions of kin of freed Amers",
            "url",
            1,
            true
        )

        val artistName = "Patricio Rey y sus Redonditos de Ricota"

        val result = articleDescriptionHelper.textToHtml(article, artistName)

        val expected = StringBuilder("[*]reactions of kin of freed Amers")

        Assert.assertEquals(expected, result) //Esta bien usar SpannableString ??? se pide Spannable

    }

    @Test
    fun `given a non local article it should return the description`() {

        val article: Article = NYArticle(
            "reactions of kin of freed Amers",
            "url",
            1,
            true
        )

        val artistName = "Patricio Rey y sus Redonditos de Ricota"

        val result = articleDescriptionHelper.textToHtml(article, artistName)

        val expected = StringBuilder("reactions of kin of freed Amers")

        Assert.assertEquals(expected, result) //Esta bien usar SpannableString ??? se pide Spannable

    }

    @Test // Esto va??
    fun `given a non New York article it should return the empty NYArticle description`() {
        val song: Article = mockk() // que es un mockk?

        val result = articleDescriptionHelper.textToHtml(song, "")

        val expected = "Article not found"

        Assert.assertEquals(expected, result)
    }
}