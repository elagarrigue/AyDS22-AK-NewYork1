package ayds.newyork.songinfo.moredetails.model.repository.external.infos

import com.google.gson.Gson
import com.google.gson.JsonObject
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import java.lang.StringBuilder

interface NYTimesToInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?, artistName: String): NYArticle?
}

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"

internal class JsonToInfoResolver : NYTimesToInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?, artistName:String): NYArticle? =
        try {
            serviceData?.getResponse()?.let { item ->
                NYArticle(
                    item.getArtistInformation(artistName),
                    item.getUrl()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getResponse(): JsonObject {
        val infoJson = Gson().fromJson(this, JsonObject::class.java)
        return infoJson[RESPONSE].asJsonObject
    }

    private fun JsonObject.getArtistInformation(artistName: String): String {
        return textToHtml(getCleanAbstract(), artistName)
    }

    private fun JsonObject.getCleanAbstract():String{
        val abstract = this[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]
        return abstract.asString.replace("\\n", "\n")
    }

    private fun JsonObject.getUrl(): String {
        return this[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    private fun textToHtml(text: String, termToBold: String): String {
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