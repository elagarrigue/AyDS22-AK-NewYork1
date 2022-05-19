package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import java.lang.StringBuilder

interface ArticleDescriptionHelper {
    fun textToHtml(article: Article, artistName: String): String
}

private const val SONG_FOUND_LOCAL = "[*]"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val ARTICLE_NOT_FOUND = "article not found"


internal class ArticleDescriptionHelperImpl : ArticleDescriptionHelper {
    override fun textToHtml(article: Article, artistName: String): String =
        when (article) {
            is NYArticle -> getFormattedArticleText(article, artistName)
            else -> getArticleNotFoundText()
        }

    private fun getFormattedArticleText(article: Article, artistName: String): String =
        StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            if (article.isLocallyStored)
                append(SONG_FOUND_LOCAL)
            append(formatTextToHtmlWithBoldTerm(article.articleInformation, artistName))
            append(HTML_END_TAGS)
        }.toString()

    private fun getArticleNotFoundText(): String =
        StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            append(ARTICLE_NOT_FOUND)
            append(HTML_END_TAGS)
        }.toString()

    private fun formatTextToHtmlWithBoldTerm(text: String, termToBold: String): String {
        return text.replace("'", " ")
            .replace("\n", "<br>")
            .replace(termToBold.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
    }
}