package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.text.Html
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
import ayds.newyork.songinfo.utils.UtilsInjector.navigationUtils
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject

private const val SONG_FOUND_LOCAL = "[*]"
private const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    private lateinit var articlePane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private val articleDescriptionHelper: ArticleDescriptionHelper =
        MoreDetailsViewInjector.articleDescriptionHelper

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initInjector()
        initArtistName()
        initProperties()
        initListeners()
        initObservers()
        notifyMoreInfoAction()
        updateSongImage()
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun initInjector() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initArtistName() {
        uiState = uiState.copy(artistName = intent.getStringExtra(ARTIST_NAME).toString())
    }

    private fun initProperties() {
        articlePane = findViewById(R.id.articlePane)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener { notifyOpenMoreInfoUrlAction() }
    }

    private fun initObservers() {
        moreDetailsModel.articleObservable
            .subscribe { value -> updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistArticle: Article) {
        updateUIState(artistArticle)
        updateArtistDescription()
    }

    private fun updateUIState(artistArticle: Article) {
        when (artistArticle) {
            is NYArticle -> updateUIArtistInfo(artistArticle)
            EmptyArticle -> updateUIArtistInfoNotFound()
        }
    }

    private fun updateUIArtistInfo(artistArticle: Article) {
        uiState =
            uiState.copy(
                artistInfo = if (artistArticle.isLocallyStored) SONG_FOUND_LOCAL else {
                    ""
                } + artistArticle.articleInformation,
                artistUrl = artistArticle.articleUrl
            )
    }

    private fun updateUIArtistInfoNotFound() {
        uiState = uiState.copy(
            artistUrl = "",
            artistInfo = ""
        )
    }

    private fun updateArtistDescription() {
        runOnUiThread {
            val articleFormatted =
                articleDescriptionHelper.textToHtml(uiState.artistInfo, uiState.artistName)
            articlePane.text = Html.fromHtml(articleFormatted)
        }
    }

    private fun updateSongImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(uiState.imageUrl, imageView)
        }
    }

    private fun notifyMoreInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)
    }

    private fun notifyOpenMoreInfoUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)
    }


}