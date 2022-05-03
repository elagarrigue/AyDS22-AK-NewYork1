package ayds.newyork.songinfo.moredetails.model.repository.external.infos

import com.google.gson.Gson
import com.google.gson.JsonObject
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface NYTimesToInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}
private const val TEST = "items"
private const val ID = "id"

internal class JsonToInfoResolver(
) : NYTimesToInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo? =
        try {
            serviceData?.getFirstItem()?.let {  item ->
                ArtistInfo(
                    item.getId(),
                    item.getArtistName(),
                    item.getArtistInformation(),
                    item.getUrl()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getFirstItem(): JsonObject {
        //TODO!
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val tracks = jobj[TEST].asJsonObject
        val items = tracks[TEST].asJsonArray
        return items[0].asJsonObject
    }
    private fun JsonObject.getId() = this[ID].asInt

    private fun JsonObject.getUrl(): String {
        //TODO!
        val album = this[TEST].asJsonObject
        return album[TEST].asJsonArray[1].asJsonObject[TEST].asString
    }
    private fun JsonObject.getArtistName(): String {
        //TODO!
        val album = this[TEST].asJsonObject
        return album[TEST].asJsonArray[1].asJsonObject[TEST].asString
    }
    private fun JsonObject.getArtistInformation(): String {
        //TODO!
        val album = this[TEST].asJsonObject
        return album[TEST].asJsonArray[1].asJsonObject[TEST].asString
    }

}