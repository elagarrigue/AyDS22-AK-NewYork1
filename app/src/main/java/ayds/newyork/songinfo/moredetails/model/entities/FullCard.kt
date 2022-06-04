package ayds.newyork.songinfo.moredetails.model.entities

interface Card {
    val description: String
    val infoURL: String
    val artistName: String
    val source: InfoSource
    val sourceLogoURL: String
    var isLocallyStored: Boolean
}

data class FullCard(
    override val description: String,
    override val infoURL: String,
    override val artistName: String,
    override val source:InfoSource = InfoSource.NoSource,
    override val sourceLogoURL: String = "",
    override var isLocallyStored: Boolean = false

) : Card

object EmptyCard : Card {
    override val description: String = ""
    override val infoURL: String = ""
    override val artistName: String = ""
    override val source: InfoSource=InfoSource.NoSource
    override val sourceLogoURL: String = ""
    override var isLocallyStored: Boolean = false
}

