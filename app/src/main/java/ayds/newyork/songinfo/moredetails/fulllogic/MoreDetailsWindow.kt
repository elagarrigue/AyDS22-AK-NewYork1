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
import java.lang.StringBuilder

private const val ARTIST_NAME = "artistName"
private const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END_TAGS = "</font></div></html>"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
private const val SONG_FOUND_LOCAL = "[*]"

class MoreDetailsWindow : AppCompatActivity() {
    private lateinit var articlePane: TextView
    private lateinit var openUrlButton: View
    private lateinit var imageView: ImageView
    private val nyAPI = getNYAPI()
    private val artistInfoStorage: ArtistInfoStorage = ArtistInfoStorageImpl(this)

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        updateArtistInfo()
    }

    private fun initProperties() {
        articlePane = findViewById(R.id.articlePane)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun updateArtistInfo() {
        val artistNameToSearch = intent.getStringExtra(ARTIST_NAME).toString()
        getArtistInfo(artistNameToSearch)
    }

    private fun getArtistInfo(artistName: String) {
        Thread {
            searchArtist(artistName)
        }.start()
    }

    private fun searchArtist(artistName: String) {
        var artistInfo = artistInfoStorage.getArtistInfo(artistName)

        artistInfo?.let {
            SONG_FOUND_LOCAL + artistInfo
        } ?: run {
            val artistInfoJson = searchWithExternalService(artistName)
            artistInfo = artistInfoJson?.let { saveArtistInLocalStorage(artistName, it) }
        }

        updateUrlButton(artistName)
        updateArtistData(artistInfo!!)
    }

    private fun searchWithExternalService(artistName: String): JsonElement? =
        nyAPI.getResponse(artistName).getAbstract()

    private fun getUrl(artistName: String) =
        nyAPI.getResponse(artistName).getUrl()

    private fun NYTimesAPI.getResponse(artistName: String?): JsonObject {
        val callResponse = this.getArtistInfo(artistName).execute()
        val infoJson = Gson().fromJson(callResponse.body(), JsonObject::class.java)
        return infoJson[RESPONSE].asJsonObject
    }

    private fun JsonObject.getAbstract() = this[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]

    private fun JsonObject.getUrl() = this[DOCS].asJsonArray[0].asJsonObject[WEB_URL]

    private fun saveArtistInLocalStorage(
        artistName: String,
        abstract: JsonElement
    ): String {
        val cleanAbstract = abstract.asString.replace("\\n", "\n")
        val info = textToHtml(cleanAbstract, artistName)
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

    private fun updateUrlButton(artistName: String) {
        val url = getUrl(artistName)
        val urlString = url.asString

        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun updateArtistData(artistNameDB: String) {
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(imageView)
            articlePane.text = Html.fromHtml(artistNameDB)
        }
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