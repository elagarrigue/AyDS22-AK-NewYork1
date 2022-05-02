package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.Info
import ayds.newyork.songinfo.moredetails.model.repository.local.NYLocalStorage

internal class ArticleLocalStorageImpl(context: Context?) : NYLocalStorage,
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ARTIST_ID_COLUMN,
        ARTIST_NAME_COLUMN,
        URL_COLUMN,
        INFO_COLUMN
    )

    override fun getArtistInfo(artistName: String): Info {
        TODO("FALTA")
    }

    override fun saveArtist(artistName: String, article: Info) {
        val values = ContentValues().apply {
            put(ARTIST_NAME_COLUMN, artistName)
            put(INFO_COLUMN, article.artistInformation)
            put(URL_COLUMN, article.articleUrl)
            put(SOURCE_COLUMN, SOURCE_VALUE)
        }
        writableDatabase?.insert(ARTISTS_TABLE_NAME, null, values)
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}