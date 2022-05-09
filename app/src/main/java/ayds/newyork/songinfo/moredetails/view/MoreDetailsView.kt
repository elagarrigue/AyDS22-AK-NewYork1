package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.Article
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArticle
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject

private const val SONG_FOUND_LOCAL = "[*]"
private const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    private lateinit var articlePane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initInjector()
        initArtistName()
        initProperties()
        initListeners()
        initObservers()
        updateSongImage()
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener { notifyMoreInfoAction() }
    }

    private fun initArtistName() {
        uiState = uiState.copy(artistName = intent.getStringExtra(ARTIST_NAME).toString())
    }

    private fun initInjector() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initProperties() {
        articlePane = findViewById(R.id.articlePane)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun initObservers() {
        moreDetailsModel.articleObservable
            .subscribe { value -> updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistArticle: Article) {
        updateUIstate(artistArticle)
        updateUrlButton()
        updateArtistDescription()
    }

    private fun updateUIstate(artistArticle: Article) {
        when (artistArticle) {
            is NYArticle -> updateUIartistInfo(artistArticle)
            EmptyArticle -> updateUIartistInfoNotFound()
        }
    }

    private fun updateUIartistInfo(artistArticle: Article) {
        if (artistArticle.isLocallyStored)
            uiState = uiState.copy(artistInfo = SONG_FOUND_LOCAL + artistArticle.articleInformation)
        else
            uiState = uiState.copy(artistInfo = artistArticle.articleInformation)
        uiState = uiState.copy(artistUrl = artistArticle.articleUrl)
    }

    private fun updateUIartistInfoNotFound() {
        uiState = uiState.copy(artistUrl = "")
        uiState = uiState.copy(artistInfo = "")
    }

    private fun updateUrlButton() {
        runOnUiThread {
            //openUrlButton = uiState.artistUrl
        }
    }

    private fun updateArtistDescription() {
        runOnUiThread {
            articlePane.text = uiState.artistInfo
        }
    }

    private fun updateSongImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(uiState.defaultImageUrl, imageView)
        }
    }

    private fun notifyMoreInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)
    }

}