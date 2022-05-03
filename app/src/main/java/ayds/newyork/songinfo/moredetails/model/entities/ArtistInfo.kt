package ayds.newyork.songinfo.moredetails.model.entities

interface Info {
    val artistId: Int
    val artistName: String
    val artistInformation: String
    val articleUrl: String
    var isLocallyStored: Boolean
}

data class ArtistInfo(
    override val artistId: Int,
    override val artistName: String,
    override val artistInformation: String,
    override val articleUrl: String,
    override var isLocallyStored: Boolean=false
) : Info

object EmptyInfo: Info{
    override val artistId: Int =0
    override val artistName: String=""
    override val artistInformation: String=""
    override val articleUrl: String = ""
    override var isLocallyStored: Boolean=false

}

