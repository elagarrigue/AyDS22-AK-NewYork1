package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.Info
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage

internal class ArticleLocalStorageImpl(
    context: Context,
    private val cursorToArticleMapper: CursorToArticleMapper
) : NYLocalStorage,
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ARTICLE_ID_COLUMN,
        ARTIST_NAME_COLUMN,
        ARTICLE_URL_COLUMN,
        ARTICLE_INFO_COLUMN
    )

    override fun getArtistInfo(artistName: String): Info {
        val cursor = readableDatabase.query(
            ARTICLES_TABLE_NAME,
            projection,
            "$ARTIST_NAME_COLUMN = ?",
            arrayOf(artistName),
            null,
            null,
            ARTICLE_DESC
        )
        return cursorToArticleMapper.map(cursor)
    }

    override fun saveArtist(artistName: String, article: Info) {
        val values = ContentValues().apply {
            put(ARTIST_NAME_COLUMN, artistName)
            put(ARTICLE_INFO_COLUMN, article.artistInformation)
            put(ARTICLE_URL_COLUMN, article.articleUrl)
            put(ARTICLE_SOURCE_COLUMN, SOURCE_VALUE)
        }
        writableDatabase?.insert(ARTICLES_TABLE_NAME, null, values)
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}