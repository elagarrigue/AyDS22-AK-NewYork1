package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import com.squareup.picasso.Picasso

private const val ARTIST_NAME = "artistName"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class MoreDetailsView : AppCompatActivity() {

    private lateinit var articlePane: TextView
    private lateinit var openUrlButton: View
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initGUI()
        updateArtistInfo()
    }

    private fun initGUI(){
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
            val artistInfo = getAPIInfo(artistName)
            updateUrlButton(artistName)
            updateArtistData(artistInfo)
        }.start()
    }

    private fun getAPIInfo(artistName: String): String {
        return ""
    }

    private fun updateUrlButton(artistName: String) {

    }

    private fun updateArtistData(artistName: String) {
        runOnUiThread {
            updateDataOnView(artistName)
        }
    }

    private fun updateDataOnView(artistName: String) {
        Picasso.get().load(IMAGE_URL).into(imageView)
        articlePane.text = Html.fromHtml(artistName)
    }

}