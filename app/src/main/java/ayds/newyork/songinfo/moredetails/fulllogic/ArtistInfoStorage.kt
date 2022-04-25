package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ARTIST_ID_COLUMN = "id"
private const val ARTISTS_TABLE_NAME = "artists"
private const val ARTIST_NAME_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"
private const val SOURCE_VALUE = 1
private const val DATABASE_NAME = "artists.db"
private const val DATABASE_VERSION = 1
private const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE_NAME (" +
            "$ARTIST_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_NAME_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"

interface ArtistInfoStorage {
    fun saveArtist(artist: String, info: String)
    fun getArtistInfo(artistName: String): String?
}

class ArtistInfoStorageImpl(context: Context?) : ArtistInfoStorage,
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ARTIST_ID_COLUMN,
        ARTIST_NAME_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: String, info: String) {
        val values = ContentValues().apply {
            put(ARTIST_NAME_COLUMN, artist)
            put(INFO_COLUMN, info)
            put(SOURCE_COLUMN, SOURCE_VALUE)
        }
        writableDatabase?.insert(ARTISTS_TABLE_NAME, null, values)
    }

    override fun getArtistInfo(artistName: String): String? {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE_NAME,
            projection,
            "$ARTIST_NAME_COLUMN = ?",
            arrayOf(artistName),
            null,
            null,
            "$ARTIST_NAME_COLUMN DESC"
        )
        return mapToList(cursor)?.get(0)
    }

    private fun mapToList(cursor: Cursor): MutableList<String>? {
        val informationCollection: MutableList<String> = ArrayList()
        with(cursor) {
            if (moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(INFO_COLUMN)
                )
                informationCollection.add(info)
            } else
                return null
        }
        return informationCollection
    }
}
