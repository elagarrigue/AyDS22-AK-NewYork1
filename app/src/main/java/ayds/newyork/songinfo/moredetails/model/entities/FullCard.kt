package ayds.newyork.songinfo.moredetails.model.entities

interface Card {
    val description: String
    val infoURL: String
    val artistName: String
    val source: String
    val sourceLogoURL: String
    var isLocallyStored: Boolean
}

data class FullCard(
    override val description: String,
    override val infoURL: String,
    override val artistName: String,
    override val source:String,
    override val sourceLogoURL: String = "",
    override var isLocallyStored: Boolean = false

) : Card

object EmptyCard : Card {
    override val description: String = ""
    override val infoURL: String = ""
    override val artistName: String = ""
    override val source: String=""
    override val sourceLogoURL: String = ""
    override var isLocallyStored: Boolean = false
}

