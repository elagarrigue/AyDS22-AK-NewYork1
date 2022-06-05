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
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
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
    private lateinit var cardList: List<Card>

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

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

    private fun initDummyCards(){
        val dummyCard1 = FullCard("descNY","https://www.nytimes.com/es/","artName", InfoSource.NewYorkTimes,"https://fonktown.es/wp-content/uploads/2020/01/nytimes-logo-blanco.png",false)
        val dummyCard2 = FullCard("descWIK","https://en.wikipedia.org/wiki/Main_Page","artName", InfoSource.Wikipedia,"https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Wikipedia-logo-v2.svg/1200px-Wikipedia-logo-v2.svg.png",false)
        val dummyCard3 = FullCard("descLFM","https://www.last.fm/es/","artName", InfoSource.LastFM,"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/1280px-Lastfm_logo.svg.png",false)
        val dummyCardEmpty = EmptyCard
        val dummyCardList = mutableListOf<Card>()
        dummyCardList.add(0,dummyCard1)
        //dummyCardList.add(1,dummyCard2)
        dummyCardList.add(1,dummyCardEmpty)
        dummyCardList.add(2,dummyCard3)
        cardList = dummyCardList
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        searchCard(sourceSpinner.selectedItem.toString()).apply {
            updateArtistInfo(this)
        }
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
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, InfoSource.values())
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceSpinner.adapter = aa
        sourceSpinner.onItemSelectedListener = this
        //verificar que 0 sea NYTimes
        sourceSpinner.setSelection(0)
    }

    private fun initListeners() {
        openUrlButton.setOnClickListener { notifyOpenMoreInfoUrlAction() }
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable
            .subscribe { value ->
                //cardList = value
                initDummyCards()
                //de alguna manera hay que asegurarse de que en el enum, NYTimes sea el primero declarado para que esto funcione
                searchCard(InfoSource.values().first().toString()).apply {
                    updateArtistInfo(this)
                }
            }
    }

    private fun updateArtistInfo(artistCard: Card) {
        updateUIArtistInfo(artistCard)
        updateArtistDescription(artistCard)
        updateSourceDescription(artistCard)
        updateUrlButton(artistCard)
        updateSongImage()
    }

    private fun updateUIArtistInfo(artistCard: Card) {
        uiState = uiState.copy(
            artistInfo = artistCard.description,
            artistUrl = artistCard.infoURL,
            imageUrl = artistCard.sourceLogoURL,
        )
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
            if(uiState.imageUrl!="")
                imageLoader.loadImageIntoView(uiState.imageUrl, imageView)
        }
    }

    private fun searchCard(sourceName: String): Card {
        for (card in cardList) {
            if (card.source.toString() == sourceName) {
                return card
            }
        }
        return EmptyCard
    }

    private fun notifyMoreInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ShowInfoArticle)
    }

    private fun notifyOpenMoreInfoUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenMoreInfoUrl)
    }

}