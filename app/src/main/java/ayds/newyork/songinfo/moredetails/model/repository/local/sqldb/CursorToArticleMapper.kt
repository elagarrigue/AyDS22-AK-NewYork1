package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo

interface CursorToArticleMapper {
    fun map(cursor: Cursor): ArtistInfo?
}

internal class CursorToArticleMapperImpl : CursorToArticleMapper{
    override fun map(cursor: Cursor): ArtistInfo?  {
        with(cursor) {
            return if (moveToNext()) {
                ArtistInfo(
                    artistInformation = getString(cursor.getColumnIndexOrThrow(ARTICLE_INFO_COLUMN)),
                    articleUrl = getString(cursor.getColumnIndexOrThrow(ARTICLE_URL_COLUMN))
                )
            } else {
                null
            }

    }
}
}