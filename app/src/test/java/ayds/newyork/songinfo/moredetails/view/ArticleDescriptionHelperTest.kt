package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import org.junit.Assert
import org.junit.Test

private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"

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

        var result = articleDescriptionHelper.textToHtml(article, artistName)
        result = result.replace(HTML_DIV_WIDTH, "")
        result = result.replace(HTML_FONT, "")
        result = result.replace(HTML_END_TAGS, "")

        val expected = "[*]reactions of kin of freed Amers"

        Assert.assertEquals(expected, result)

    }

    @Test
    fun `given a non local article it should return the description`() {

        val articleDescription = "Mr Cerati had platinum success in " +
                "the Spanish rock and pop world and worked " +
                "with artist like Shakira"
        val article: Article = NYArticle(
            articleDescription,
            "url",
            1,
            false
        )

        val artistName = "Gustavo Cerati"

        var result = articleDescriptionHelper.textToHtml(article, artistName)
        result = result.replace(HTML_DIV_WIDTH, "")
        result = result.replace(HTML_FONT, "")
        result = result.replace(HTML_END_TAGS, "")
        
        Assert.assertEquals(articleDescription, result)

    }

}
