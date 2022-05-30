package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.FullCard

interface CursorToCardMapper {
    fun map(cursor: Cursor): FullCard?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {
    override fun map(cursor: Cursor): FullCard? {
        with(cursor) {
            return if (moveToNext()) {
                FullCard(
                    description = getString(cursor.getColumnIndexOrThrow(CARD_INFO_COLUMN)),
                    infoURL = getString(cursor.getColumnIndexOrThrow(CARD_URL_COLUMN)),
                    artistName = getString(cursor.getColumnIndexOrThrow(ARTIST_NAME_COLUMN)),
                )
            } else {
                null
            }

        }
    }
}