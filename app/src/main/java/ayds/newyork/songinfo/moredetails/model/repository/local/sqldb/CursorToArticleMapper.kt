package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.NYArticle

interface CursorToArticleMapper {
    fun map(cursor: Cursor, artistName: String): NYArticle?
}

internal class CursorToArticleMapperImpl : CursorToArticleMapper {
    override fun map(cursor: Cursor, artistName: String): NYArticle? {
        with(cursor) {
            return if (moveToNext()) {
                NYArticle(
                    articleInformation = getString(cursor.getColumnIndexOrThrow(ARTICLE_INFO_COLUMN)),
                    articleUrl = getString(cursor.getColumnIndexOrThrow(ARTICLE_URL_COLUMN)),
                    1,
                    true,
                    artistName
                )
            } else {
                null
            }

        }
    }
}