package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfoSong
import ayds.newyork.songinfo.moredetails.model.entities.EmptyInfo
import ayds.newyork.songinfo.moredetails.model.entities.Info

interface CursorToArticleMapper {
    fun map(cursor: Cursor): Info
}

internal class CursorToArticleMapperImpl : CursorToArticleMapper{
    override fun map(cursor: Cursor): Info {
        with(cursor) {
            if (moveToNext()) {
                return ArtistInfoSong(
                    artistId = getInt(cursor.getColumnIndexOrThrow(ARTICLE_ID_COLUMN)),
                    artistName = getString(cursor.getColumnIndexOrThrow(ARTIST_NAME_COLUMN)),
                    artistInformation = getString(cursor.getColumnIndexOrThrow(ARTICLE_INFO_COLUMN)),
                    articleUrl = getString(cursor.getColumnIndexOrThrow(ARTICLE_URL_COLUMN))
                )
            }
        }
        return EmptyInfo
    }
}