package ayds.newyork.songinfo.moredetails.model.entities

interface Info {
    val artistInformation: String
    val articleUrl: String
    var isLocallyStored: Boolean
}

data class ArtistInfo(
    override val artistInformation: String,
    override val articleUrl: String,
    override var isLocallyStored: Boolean=false
) : Info

object EmptyInfo: Info{
    override val artistInformation: String=""
    override val articleUrl: String = ""
    override var isLocallyStored: Boolean=false

}

