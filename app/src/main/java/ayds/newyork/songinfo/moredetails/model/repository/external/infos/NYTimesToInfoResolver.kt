package ayds.newyork.songinfo.moredetails.model.repository.external.infos

import com.google.gson.Gson
import com.google.gson.JsonObject
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYTimesToInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"

internal class JsonToInfoResolver(
) : NYTimesToInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo? =
        try {
            serviceData?.getResponse()?.let { item ->
                ArtistInfo(
                    item.getArtistInformation(),
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

    private fun JsonObject.getArtistInformation(): String {
        return this[DOCS].asJsonArray[0].asJsonObject[ABSTRACT].asString
    }

    private fun JsonObject.getUrl(): String {
        return this[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }
}