package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.entities.Info
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState
}

class MoreDetailsViewImpl : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    private lateinit var articlePane: TextView
    private lateinit var openUrlButton: View
    private lateinit var imageView: ImageView
    private lateinit var moreDetailsModel: MoreDetailsModel

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initGUI()
        initObservers()
    }

    private fun initGUI(){
        articlePane = findViewById(R.id.articlePane)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun initObservers() {
        moreDetailsModel.articleObservable
            .subscribe { value -> updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistInfo: Info) {
        updateUrlButton(artistInfo.articleUrl)
        updateArtistData(artistInfo.artistInformation)
    }

    private fun updateUrlButton(articleUrl: String) {
        //uiState = uiState.copy(url = artistName)
    }

    private fun updateArtistData(artistInfo: String) {
        runOnUiThread {
            updateDataOnView(artistInfo)
        }
    }

    private fun updateDataOnView(artistDesc: String) {
        uiState = uiState.copy(
            //logoUrl = IMAGE_URL,
            //artistDescription = artistDesc
        )
    }

    private fun notifyMoreInfoAction() {
        //onActionSubject.notify(MoreDetailsUiEvent.OpenInfoUrl)
    }

}