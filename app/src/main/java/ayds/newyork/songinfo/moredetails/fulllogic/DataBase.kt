package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ARTISTS_TABLE_NAME="artists"
private const val ARTIST_NAME_COLUMN="artist"
private const val INFO_COLUMN="info"
private const val SOURCE_COLUMN="source"
private const val SOURCE_VALUE=1

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String?, info: String?) {
        val values = ContentValues().apply {
            put(ARTIST_NAME_COLUMN,artist)
            put(INFO_COLUMN, info)
            put(SOURCE_COLUMN, SOURCE_VALUE)
        }
        writableDatabase?.insert(ARTISTS_TABLE_NAME, null, values)
    }

    fun getInfo(dbHelper: DataBase, artist: String): String? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            "id",
            "artist",
            "info"
        )
        val selection = "artist  = ?"
        val selectionArgs = arrayOf(artist)
        val sortOrder = "artist DESC"
        val cursor = db.query(
            "artists",
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow("info")
            )
            items.add(info)
        }
        cursor.close()
        return if (items.isEmpty()) null else items[0]
    }
}
