package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import java.lang.StringBuilder

interface CardDescriptionHelper {
    fun textToHtml(card: Card): String
    fun sourceToHtml(card: Card): String
}

private const val SONG_FOUND_LOCAL = "[*]"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val CARD_NOT_FOUND = "article not found"


internal class CardDescriptionHelperImpl : CardDescriptionHelper {
    override fun textToHtml(card: Card): String =
        when (card) {
            is FullCard -> getFormattedCardText(card)
            else -> getCardNotFoundText()
        }

    override fun sourceToHtml(card: Card): String =
        StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            append("Fuente: ")
            when (card.source){
                "NYArticle" ->append("New York Times")
                else ->append("No Encontrada")
            }

            append(HTML_END_TAGS)
        }.toString()

    private fun getFormattedCardText(card: Card): String =
        StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            if (card.isLocallyStored)
                append(SONG_FOUND_LOCAL)
            append(formatTextToHtmlWithBoldTerm(card.description, card.artistName))
            append(HTML_END_TAGS)
        }.toString()

    private fun getCardNotFoundText(): String =
        StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            append(CARD_NOT_FOUND)
            append(HTML_END_TAGS)
        }.toString()

    private fun formatTextToHtmlWithBoldTerm(text: String, termToBold: String): String {
        return text.replace("'", " ")
            .replace("\n", "<br>")
            .replace(termToBold.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
    }
}