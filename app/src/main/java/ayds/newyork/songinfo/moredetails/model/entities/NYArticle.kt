package ayds.newyork.songinfo.moredetails.model.entities

interface Article {
    val articleInformation: String
    val articleUrl: String
    val source: Int
    var isLocallyStored: Boolean
    val artistName: String
}

data class NYArticle(
    override val articleInformation: String,
    override val articleUrl: String,
    override val source: Int = 1,
    override var isLocallyStored: Boolean = false,
    override val artistName: String
) : Article

object EmptyArticle : Article {
    override val articleInformation: String = ""
    override val articleUrl: String = ""
    override val source: Int = -1
    override var isLocallyStored: Boolean = false
    override val artistName: String = ""
}

