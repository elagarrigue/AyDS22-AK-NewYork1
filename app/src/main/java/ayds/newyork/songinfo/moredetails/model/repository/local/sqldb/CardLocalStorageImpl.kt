package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.FullCard
import ayds.newyork.songinfo.moredetails.model.repository.local.CardLocalStorage

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToArticleMapper: CursorToCardMapper
) : CardLocalStorage,
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        CARD_ID_COLUMN,
        ARTIST_NAME_COLUMN,
        CARD_URL_COLUMN,
        CARD_INFO_COLUMN,
        CARD_SOURCE_COLUMN
    )

    override fun getCard(artistName: String): FullCard? {
        val cursor = readableDatabase.query(
            CARDS_TABLE_NAME,
            projection,
            "$ARTIST_NAME_COLUMN = ?",
            arrayOf(artistName),
            null,
            null,
            CARD_DESC
        )
        return cursorToArticleMapper.map(cursor)
    }

    override fun saveCard(artistName: String, card: FullCard) {
        val values = ContentValues().apply {
            put(ARTIST_NAME_COLUMN, artistName)
            put(CARD_INFO_COLUMN, card.description)
            put(CARD_URL_COLUMN, card.infoURL)
            put(CARD_SOURCE_COLUMN, card.source)
        }
        writableDatabase?.insert(CARDS_TABLE_NAME, null, values)
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(createCardsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}