package ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.home.model.entities.DatePrecision
import ayds.newyork.songinfo.home.model.entities.SpotifySong
import java.sql.SQLException

interface CursorToSpotifySongMapper {

    fun map(cursor: Cursor): SpotifySong?
}

internal class CursorToSpotifySongMapperImpl : CursorToSpotifySongMapper {

    override fun map(cursor: Cursor): SpotifySong? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    val storedPrecisionOrdinal = cursor.getInt(getColumnIndexOrThrow(RELEASE_DATE_PRECISION_COLUMN))
                    val releaseDatePrecision = DatePrecision.values()[storedPrecisionOrdinal]
                    SpotifySong(
                        id = getString(getColumnIndexOrThrow(ID_COLUMN)),
                        songName = getString(getColumnIndexOrThrow(NAME_COLUMN)),
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        albumName = getString(getColumnIndexOrThrow(ALBUM_COLUMN)),
                        releaseDate = getString(getColumnIndexOrThrow(RELEASE_DATE_COLUMN)),
                        releaseDatePrecision = releaseDatePrecision,
                        spotifyUrl = getString(getColumnIndexOrThrow(SPOTIFY_URL_COLUMN)),
                        imageUrl = getString(getColumnIndexOrThrow(IMAGE_URL_COLUMN)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}