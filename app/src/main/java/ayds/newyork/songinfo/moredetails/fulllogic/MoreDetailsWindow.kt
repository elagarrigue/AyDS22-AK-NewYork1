package ayds.newyork.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.newyork.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import android.content.Intent
import android.net.Uri
import com.squareup.picasso.Picasso
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.google.gson.JsonElement
import java.io.IOException
import java.lang.StringBuilder

private const val ARTIST_NAME = "artistName"
private const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val NO_RESULTS = "No Results"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class MoreDetailsWindow : AppCompatActivity() {
    private lateinit var articlePane: TextView
    private val artistInfoStorage: ArtistInfoStorage = ArtistInfoStorageImpl(this)

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        articlePane = findViewById(R.id.textPane2)
        getArtistInfo((intent.getStringExtra(ARTIST_NAME)))
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            var artistInfo = artistName?.let { artistInfoStorage.getArtistInfo(it) }
            artistInfo = if (artistInfo != null)
                "[*]$artistInfo"
            else
                searchWithExternalService(artistName)
            updateArtistData(artistInfo)
        }.start()
    }

    private fun searchWithExternalService(artistName: String?): String {
        lateinit var infoToReturn: String
            try {
                val response = getNYAPI().getResponse(artistName)
                val abstract = response.getAbstract()
                val url = response.getUrl()
                infoToReturn = if (abstract == null)
                    NO_RESULTS
                else
                    saveArtistInLocalStorage(artistName, abstract)
                updateUrlButton(url)
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        return infoToReturn
    }

    private fun NYTimesAPI.getResponse(artistName: String?): JsonObject {
        val callResponse = this.getArtistInfo(artistName).execute()
        val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
        return jobj[RESPONSE].asJsonObject
    }

    private fun JsonObject.getAbstract() = this[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]

    private fun JsonObject.getUrl() = this[DOCS].asJsonArray[0].asJsonObject[WEB_URL]

    private fun saveArtistInLocalStorage(
        artistName: String?,
        abstract: JsonElement
    ): String {
        var info = abstract.asString.replace("\\n", "\n")
        info = textToHtml(info, artistName)
        artistInfoStorage.saveArtist(artistName, info)
        return info
    }

    private fun getNYAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    private fun updateUrlButton(url: JsonElement) {
        val urlString = url.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun updateArtistData(artistNameDB: String) {
        val imageUrl = IMAGE_URL
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            articlePane.text = Html.fromHtml(artistNameDB)
        }
    }

    private fun textToHtml(text: String, termToBold: String?): String {
        return StringBuilder().apply {
            append(HTML_DIV_WIDTH)
            append(HTML_FONT)
            append(formatTextToHtmlWithBoldTerm(text, termToBold))
            append(HTML_END_TAGS)
        }.toString()
    }

    private fun formatTextToHtmlWithBoldTerm(text: String, termToBold: String?): String {
        return text.replace("'", " ")
            .replace("\n", "<br>")
            .replace(termToBold!!.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
    }

}