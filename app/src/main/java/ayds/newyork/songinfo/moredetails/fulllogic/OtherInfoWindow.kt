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

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        getArtistInfo((intent.getStringExtra("artistName")))
    }

    private fun getArtistInfo(artistName: String?) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val apiNYTimes = retrofit.create(NYTimesAPI::class.java)
        Thread {
            var artistNameDB = DataBase.getInfo(dataBase, artistName)
            if (artistNameDB != null) {
                artistNameDB = "[*]$artistNameDB"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = apiNYTimes.getArtistInfo(artistName).execute()
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj["response"].asJsonObject
                    val abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (abstract == null) {
                        artistNameDB = "No Results"
                    } else {
                        artistNameDB = abstract.asString.replace("\\n", "\n")
                        artistNameDB = textToHtml(artistNameDB, artistName)

                        DataBase.saveArtist(dataBase, artistName, artistNameDB)
                    }
                    updateUrlButton(url)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            updateArtistData(artistNameDB)
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
            textPane2!!.text = Html.fromHtml(artistNameDB)
        }
    }
/*
    private fun open(artist: String?) {
        dataBase = DataBase(this)
        //DataBase.saveArtist(artist, "sarasa")
        dataBase!!.saveArtist(artist, null)
        getArtistInfo(artist)
    }

 */

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        private fun textToHtml(text: String, termToBold: String?): String {
            val toReturn = StringBuilder()
            toReturn.append(getHtmlDivWidth())
            toReturn.append(getHtmlFont())
            val htmlText = formatTextToHtml(text)
            val textWithBoldTerm = getTextWithBoldTerm(htmlText, termToBold)
            toReturn.append(textWithBoldTerm)
            toReturn.append(htmlEndTags())

            return toReturn.toString()
        }

        private fun getHtmlDivWidth(): String {
            return "<html><div width=400>"
        }

        private fun getHtmlFont(): String {
            return "<font face=\"arial\">"
        }

        private fun formatTextToHtml(text: String): String {
            return text.replace("'", " ")
                .replace("\n", "<br>")
        }

        private fun getTextWithBoldTerm(text: String, termToBold: String?): String {
            return text.replace(termToBold!!.toRegex(), "<b>" + termToBold.uppercase() + "</b>")
        }

        private fun htmlEndTags(): String {
            return "</font></div></html>"
        }

    }
}