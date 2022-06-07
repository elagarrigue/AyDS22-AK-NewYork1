package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.ak1.newyorktimes.article.external.NYInjector
import ayds.ak1.newyorktimes.article.external.URL_NYTIMES_LOGO
import ayds.lisboa2.lastFM.LASTFM_LOGO
import ayds.lisboa2.lastFM.LastFMInjector
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource
import ayds.winchester1.wikipedia.WikipediaInjector
import org.junit.Assert
import org.junit.Test

private const val ARTIST = "DJ Scheme"
private const val DESCRIPTION_NY =
    "People who rely on private pension plans to pay for a dignified retirement these days are usually sorry. Few of the plans are indexed to the cost of living, and as a result pensioners typically find themselves trying to get by on yesterday's dollars at today's prices. That is why a scheme suggested by the Rockefeller Foundation--and actually put into operation over the past five years for the foundation's own pensioners--is worth examining.  "
private const val INFO_URL_NY =
    "https://www.nytimes.com/1980/07/26/archives/a-promising-pension-rescue-scheme.html"

private const val DESCRIPTION_LASTFM =
    "Gabriel Guerra (b. May 23, 1997) known professionally as DJ Scheme is a producer, artist, and is one of Juice WRLD's friends whom is apart of XXXTENTACION's collective 'Members Only'. Scheme is also a producer and has a album as of 2021, by the name of FAMILY with features from Danny Towers, Lil Yachty, Skrillex, Zacari, Joey BadA\$\$, Ski Mask The Slump God, Danny Towers, Lil Mosey, Ty Dolla \$ign, YBN Cordae, Lil Gnar, Shakewell, iann dior, TheHxliday, Tes X, \$NOT, Fenix Flexin, Jackboy, Robb Bank\$, Lil Keed, Cris Dinero, & G.Wakai. Nothing more is known about DJ Scheme other than he was a close friend to the two late rappers Juice WRLD and XXXTENTACION. <a href=\"https://www.last.fm/music/Dj+Scheme\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."
private const val INFO_URL_LASTFM = "https://www.last.fm/music/Dj+Scheme"

private const val DESCRIPTION_WIKIPEDIA =
    "October 25, 2017. &quot;Video&quot;. youtube.com. Retrieved September 6, 2020. &quot;<span class=\"searchmatch\">DJ</span> <span class=\"searchmatch\">Scheme</span> Is the Man Behind Florida's Biggest Rappers&quot;. Miami New Times. October"
private const val INFO_URL_WIKI = "https://en.wikipedia.org/?curid=57925285"
private const val WIKIPEDIA_LOGO_URL =
    "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"


class ProxyTest {

    private val proxyNYTimes: Proxy = ProxyNewYorkTimes(NYInjector.nyInfoService)
    private val proxyWikipedia: Proxy = ProxyWikipedia(WikipediaInjector.wikipediaService)
    private val proxyLastFM: Proxy = ProxyLastFM(LastFMInjector.lastFMService)

    private val nyCard = FullCard(
        DESCRIPTION_NY,
        INFO_URL_NY,
        ARTIST,
        InfoSource.NewYorkTimes,
        URL_NYTIMES_LOGO
    )
    private val lastFmCard = FullCard(
        DESCRIPTION_LASTFM,
        INFO_URL_LASTFM,
        ARTIST,
        InfoSource.LastFM,
        LASTFM_LOGO
    )
    private val wikipediaCard = FullCard(
        DESCRIPTION_WIKIPEDIA,
        INFO_URL_WIKI,
        ARTIST,
        InfoSource.Wikipedia,
        WIKIPEDIA_LOGO_URL
    )

    @Test
    fun `given an existing artist it should return a correct FullCard`() {
        val articleTimes = proxyNYTimes.getCard(ARTIST)
        val articleWikipedia = proxyWikipedia.getCard(ARTIST)
        val articleLastFM = proxyLastFM.getCard(ARTIST)

        Assert.assertEquals(nyCard, articleTimes)
        Assert.assertEquals(lastFmCard, articleLastFM)
        Assert.assertEquals(wikipediaCard, articleWikipedia)
    }

    @Test
    fun `given an non existing artist it should return an EmptyCard`() {
        val artist = "-,-,-,-,-"
        val articleTimes = proxyNYTimes.getCard(artist)
        val articleWikipedia = proxyWikipedia.getCard(artist)
        val articleLastFM = proxyLastFM.getCard(artist)

        Assert.assertEquals(EmptyCard, articleTimes)
        Assert.assertEquals(EmptyCard, articleLastFM)
        Assert.assertEquals(EmptyCard, articleWikipedia)
    }
}