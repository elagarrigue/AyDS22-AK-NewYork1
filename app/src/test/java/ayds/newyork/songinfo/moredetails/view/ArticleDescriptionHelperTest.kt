package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val SONG_FOUND_LOCAL = "[*]"
private const val ARTICLE_DESCRIPTION = "reactions of kin of freed Amers"
private const val ARTICLE_URL = "url"
private const val ARTIST_NAME_ARTICLE = "Patricio Rey y sus Redonditos de Ricota"
private const val SOURCE = 1

class ArticleDescriptionHelperTest {

    private val articleDescriptionHelper by lazy { ArticleDescriptionHelperImpl() }
    private val storedArticle = NYArticle(
        ARTICLE_DESCRIPTION,
        ARTICLE_URL,
        ARTIST_NAME_ARTICLE,
        SOURCE,
        true
    )
    private val nonStoredArticle = NYArticle(
        ARTICLE_DESCRIPTION,
        ARTICLE_URL,
        ARTIST_NAME_ARTICLE,
        SOURCE,
        false
    )

    @Test
    fun `given a local article it should return the description`() {

        val result = noHtmlFormat(articleDescriptionHelper.textToHtml(storedArticle))

        val expected = "[*]$ARTICLE_DESCRIPTION"

        Assert.assertEquals(expected, result)

    }

    @Test
    fun `given a non local article it should return the description`() {

        val result = noHtmlFormat(articleDescriptionHelper.textToHtml(nonStoredArticle))

        Assert.assertEquals(ARTICLE_DESCRIPTION, result)

    }

    @Test
    fun `given a non local article it should return the description with correct format`() {

        val expected = HTML_DIV_WIDTH + HTML_FONT + ARTICLE_DESCRIPTION + HTML_END_TAGS

        val result = articleDescriptionHelper.textToHtml(nonStoredArticle)

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a local article it should return the description with correct format`() {

        val expected =
            HTML_DIV_WIDTH + HTML_FONT + SONG_FOUND_LOCAL + ARTICLE_DESCRIPTION + HTML_END_TAGS

        val result = articleDescriptionHelper.textToHtml(storedArticle)

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non local article it should return the article not found description`() {

        val article: Article = mockk()

        val result = noHtmlFormat(articleDescriptionHelper.textToHtml(article))

        val expected = "article not found"

        Assert.assertEquals(expected, result)
    }

    private fun noHtmlFormat(text: String): String {
        var result = text.replace(HTML_DIV_WIDTH, "")
        result = result.replace(HTML_FONT, "")
        return result.replace(HTML_END_TAGS, "")
    }
}