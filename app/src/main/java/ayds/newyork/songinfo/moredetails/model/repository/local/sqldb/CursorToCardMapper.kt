package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.home.model.entities.DatePrecision
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.RELEASE_DATE_PRECISION_COLUMN
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.entities.InfoSource

interface CursorToCardMapper {
    fun map(cursor: Cursor): Card
}

internal class CursorToCardMapperImpl : CursorToCardMapper {
    override fun map(cursor: Cursor): Card {
        with(cursor) {
            return if (moveToNext()) {
                val storedInfoSourceOrdinal = cursor.getInt(getColumnIndexOrThrow(CARD_SOURCE_COLUMN))
                val infoSource = InfoSource.values()[storedInfoSourceOrdinal]
                FullCard(
                    description = getString(cursor.getColumnIndexOrThrow(CARD_INFO_COLUMN)),
                    infoURL = getString(cursor.getColumnIndexOrThrow(CARD_URL_COLUMN)),
                    artistName = getString(cursor.getColumnIndexOrThrow(ARTIST_NAME_COLUMN)),
                    source=infoSource,
                    sourceLogoURL=getString(cursor.getColumnIndexOrThrow(CARD_SOURCE_LOGO_COLUMN))
                )
            } else {
                EmptyCard
            }

        }
    }
}