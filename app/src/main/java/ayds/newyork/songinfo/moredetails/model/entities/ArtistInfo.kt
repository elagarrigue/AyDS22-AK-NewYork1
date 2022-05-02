package ayds.newyork.songinfo.moredetails.model.entities

interface Info {
    val artistId: Int
    val artistName: String
    val artistInformation: String
    var isLocallyStored: Boolean
}

data class ArtistInfoSong(
    override val artistId: Int,
    override val artistName: String,
    override val artistInformation: String,
    override var isLocallyStored: Boolean=false
) : Info

object EmptyInfo: Info{
    override val artistId: Int =0
    override val artistName: String=""
    override val artistInformation: String=""
    override var isLocallyStored: Boolean=false

}

