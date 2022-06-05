package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
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
        updateArtistInfo(uiState.cardList[uiState.positionSpinner])
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }

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
        val sources = arrayOf("LastFM", "Wikipedia", "NewYorkTimes")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, sources)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = aa
        sourceSpinner.setSelection(2, false)
        sourceSpinner.onItemSelectedListener = this

    }

    private fun initListeners() {
        openUrlButton.setOnClickListener { notifyOpenMoreInfoUrlAction() }
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable
            .subscribe { value ->
                uiState = uiState.copy(cardList = value)
                updateArtistInfo(uiState.cardList[uiState.positionSpinner])
            }
    }

    private fun updateArtistInfo(artistCard: Card) {
        updateArtistDescription(artistCard)
        updateSourceDescription(artistCard)
        updateUrlButton(artistCard)
        updateSongImage()
    }

    private fun updateArtistDescription(artistCard: Card) {
        runOnUiThread {
            with(articleDescriptionHelper.textToHtml(artistCard)) {
                cardPane.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    private fun updateSourceDescription(artistCard: Card) {
        runOnUiThread {
            with(articleDescriptionHelper.sourceToHtml(artistCard)) {
                sourcePane.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    private fun updateUrlButton(artistCard: Card) {
        runOnUiThread {
            when (artistCard) {
                is EmptyCard -> disableButton()
                else -> enableButton()
            }
        }
    }

    private fun disableButton() {
        openUrlButton.isEnabled = false
        openUrlButton.isClickable = false
    }

    private fun enableButton() {
        openUrlButton.isEnabled = true
        openUrlButton.isClickable = true
    }

    private fun updateSongImage() {
        runOnUiThread {
            if (uiState.cardList[uiState.positionSpinner].sourceLogoURL != "")
                imageLoader.loadImageIntoView(
                    uiState.cardList[uiState.positionSpinner].sourceLogoURL,
                    imageView
                )
            else
                imageLoader.loadImageIntoView(
                    "https://bitsofco.de/content/images/2018/12/broken-1.png",
                    imageView
                )
        }
    }

    private fun notifyMoreInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)
    }

    private fun notifyOpenMoreInfoUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)
    }

}