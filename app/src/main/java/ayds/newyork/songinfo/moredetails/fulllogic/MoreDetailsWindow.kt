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
import retrofit2.Response
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


class MoreDetailsWindow : AppCompatActivity() {
    private var articlePane: TextView? = null
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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val apiNYTimes = retrofit.create(NYTimesAPI::class.java)
        Thread {
            var artistNameDB = artistName?.let { artistInfoStorage.getArtistInfo(it) }
            if (artistNameDB != null) {
                artistNameDB = "[*]$artistNameDB"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = apiNYTimes.getArtistInfo(artistName).execute()
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj[RESPONSE].asJsonObject
                    val abstract = response[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]
                    val url = response[DOCS].asJsonArray[0].asJsonObject[WEB_URL]
                    if (abstract == null) {
                        artistNameDB = NO_RESULTS
                    } else {
                        artistNameDB = abstract.asString.replace("\\n", "\n")
                        artistNameDB = textToHtml(artistNameDB, artistName)

                        artistInfoStorage.saveArtist(artistName, artistNameDB)
                    }
                    updateUrlButton(url)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            if (artistNameDB != null) {
                updateArtistData(artistNameDB)
            }
        }.start()
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
        val imageUrl =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            articlePane!!.text = Html.fromHtml(artistNameDB)
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