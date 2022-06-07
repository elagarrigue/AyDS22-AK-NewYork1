package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiState.Companion.LOGO_NOT_FOUND_URL
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.UtilsInjector.navigationUtils
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject


interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView,
    AdapterView.OnItemSelectedListener {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var cardPane: TextView
    private lateinit var sourcePane: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var sourceSpinner: Spinner
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private val articleDescriptionHelper: CardDescriptionHelper =
        MoreDetailsViewInjector.cardDescriptionHelper

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState(mutableListOf())

    companion object {
        const val ARTIST_NAME = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initInjector()
        initArtistName()
        initProperties()
        initSpinner()
        initListeners()
        initObservers()
        notifyMoreInfoAction()
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        uiState = uiState.copy(positionSpinner = sourceSpinner.selectedItemPosition)
        updateArtistInfo()
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {}

    private fun initInjector() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initArtistName() {
        uiState = uiState.copy(artistName = intent.getStringExtra(ARTIST_NAME).toString())
    }

    private fun initProperties() {
        cardPane = findViewById(R.id.articlePane)
        sourcePane = findViewById(R.id.sourcePane)
        openUrlButton = findViewById(R.id.openUrlButton)
        imageView = findViewById<View>(R.id.imageView) as ImageView
        sourceSpinner = findViewById(R.id.sourceSpinner)
    }

    private fun initSpinner() {
        val sourceList = getSources()
        val positionNewYorkTimes = sourceList.indexOf(InfoSource.NewYorkTimes)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sourceList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = arrayAdapter
        sourceSpinner.setSelection(positionNewYorkTimes, false)
        sourceSpinner.onItemSelectedListener = this
        uiState = uiState.copy(positionSpinner = positionNewYorkTimes)
    }

    private fun getSources(): MutableList<InfoSource> {
        val sourceList = InfoSource.values().toMutableList()
        sourceList.remove(InfoSource.NoSource)
        return sourceList
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener { notifyOpenMoreInfoUrlAction() }
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable
            .subscribe { value ->
                uiState = uiState.copy(cardList = value)
                updateArtistInfo()
            }
    }

    private fun updateArtistInfo() {
        updateArtistDescription()
        updateSourceDescription()
        updateUrlButton()
        updateSongImage()
    }

    private fun updateArtistDescription() {
        runOnUiThread {
            with(articleDescriptionHelper.textToHtml(uiState.getCurrentCard())) {
                cardPane.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    private fun updateSourceDescription() {
        runOnUiThread {
            with(articleDescriptionHelper.sourceToHtml(uiState.getCurrentCard())) {
                sourcePane.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    private fun updateUrlButton() {
        runOnUiThread {
            when (uiState.getCurrentCard()) {
                is EmptyCard -> enableAction(false)
                else -> enableAction(true)
            }
        }
    }

    private fun enableAction(enable: Boolean) {
        openUrlButton.isEnabled = enable
        openUrlButton.isClickable = enable
    }

    private fun updateSongImage() {
        runOnUiThread {
            when (uiState.getCurrentCard()) {
                is EmptyCard -> imageLoader.loadImageIntoView(LOGO_NOT_FOUND_URL, imageView)
                else -> imageLoader.loadImageIntoView(
                    uiState.getCurrentCard().sourceLogoURL,
                    imageView
                )
            }
        }
    }

    private fun notifyMoreInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)
    }

    private fun notifyOpenMoreInfoUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)
    }

}