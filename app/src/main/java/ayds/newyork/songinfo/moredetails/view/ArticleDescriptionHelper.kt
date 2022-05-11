package ayds.newyork.songinfo.moredetails.view

import java.lang.StringBuilder

interface ArticleDescriptionHelper {
    fun textToHtml(text: String, termToBold: String): String
}

private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"

internal class ArticleDescriptionHelperImpl : ArticleDescriptionHelper{
    override fun textToHtml(text: String, termToBold: String): String {
        return StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            append(formatTextToHtmlWithBoldTerm(text, termToBold))
            append(HTML_END_TAGS)
        }.toString()
    }

    private fun formatTextToHtmlWithBoldTerm(text: String, termToBold: String): String {
        return text.replace("'", " ")
            .replace("\n", "<br>")
            .replace(termToBold.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
    }


}