package ayds.newyork.songinfo.moredetails.view

import android.text.Spanned
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.moredetails.model.entities.Article
import java.lang.StringBuilder

interface ArticleDescriptionHelper {
    fun textToHtml(article: Article, artistName: String): Spanned
}

private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val SONG_FOUND_LOCAL = "[*]"

internal class ArticleDescriptionHelperImpl : ArticleDescriptionHelper {
    override fun textToHtml(article: Article, artistName: String): Spanned {
        return HtmlCompat.fromHtml(StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            if (article.isLocallyStored)
                append(SONG_FOUND_LOCAL)
            append(formatTextToHtmlWithBoldTerm(article.articleInformation, artistName))
            append(HTML_END_TAGS)
        }.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun formatTextToHtmlWithBoldTerm(text: String, termToBold: String): String {
        return text.replace("'", " ")
            .replace("\n", "<br>")
            .replace(termToBold.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
    }


}