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
import android.util.Log
import android.view.View
import android.widget.ImageView
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null

    //private JPanel imagePanel;
    // private JLabel posterImageLabel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    fun getARtistInfo(artistName: String?) {

        // create
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val NYTimesAPI = retrofit.create(NYTimesAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        Thread {
            var text = ArtistInfoStorage.getInfo(artistInfoStorage, artistName)
            if (text != null) { // exists in db
                text = "[*]$text"
            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = NYTimesAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj["response"].asJsonObject
                    val _abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (_abstract == null) {
                        text = "No Results"
                    } else {
                        text = _abstract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)


                        // save to DB  <o/
                        ArtistInfoStorage.saveArtist(artistInfoStorage, artistName, text)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl =
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = text
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
                textPane2!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private var artistInfoStorage: ArtistInfoStorage? = null
    private fun open(artist: String?) {
        artistInfoStorage = ArtistInfoStorage(this)
        ArtistInfoStorage.saveArtist(artistInfoStorage, "test", "sarasa")
        Log.e("TAG", "" + ArtistInfoStorage.getInfo(artistInfoStorage, "test"))
        Log.e("TAG", "" + ArtistInfoStorage.getInfo(artistInfoStorage, "nada"))
        getARtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.toUpperCase() + "</b>")
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}