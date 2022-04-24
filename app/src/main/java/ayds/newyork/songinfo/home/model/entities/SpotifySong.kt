package ayds.newyork.songinfo.home.model.entities

enum class DatePrecision {
    YEAR, MONTH, DAY
}

interface Song {
    val id: String
    val songName: String
    val artistName: String
    val albumName: String
    val releaseDate: String
    val releaseDatePrecision: DatePrecision
    val spotifyUrl: String
    val imageUrl: String
    var isLocallyStored: Boolean
}

data class SpotifySong(
    override val id: String,
    override val songName: String,
    override val artistName: String,
    override val albumName: String,
    override val releaseDate: String,
    override val releaseDatePrecision: DatePrecision,
    override val spotifyUrl: String,
    override val imageUrl: String,
    override var isLocallyStored: Boolean = false
) : Song

object EmptySong : Song {
    override val id: String = ""
    override val songName: String = ""
    override val artistName: String = ""
    override val albumName: String = ""
    override val releaseDate: String = ""
    override val releaseDatePrecision: DatePrecision = DatePrecision.DAY
    override val spotifyUrl: String = ""
    override val imageUrl: String = ""
    override var isLocallyStored: Boolean = false
}
