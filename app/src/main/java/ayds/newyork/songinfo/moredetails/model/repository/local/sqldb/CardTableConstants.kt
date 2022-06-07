package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

const val CARD_ID_COLUMN = "id"
const val CARDS_TABLE_NAME = "cards"
const val ARTIST_NAME_COLUMN = "artist"
const val CARD_DESC = "artist DESC"
const val CARD_INFO_COLUMN = "info"
const val CARD_URL_COLUMN = "url"
const val CARD_SOURCE_COLUMN = "source"
const val CARD_SOURCE_LOGO_COLUMN = "logo"
const val DATABASE_NAME = "cards.db"
const val DATABASE_VERSION = 1
const val createCardsTableQuery: String =
    "create table $CARDS_TABLE_NAME (" +
            "$CARD_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_NAME_COLUMN string, " +
            "$CARD_INFO_COLUMN string, " +
            "$CARD_URL_COLUMN string, " +
            "$CARD_SOURCE_COLUMN integer, " +
            "$CARD_SOURCE_LOGO_COLUMN string)"