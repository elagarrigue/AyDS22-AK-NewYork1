package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

const val ARTIST_ID_COLUMN = "id"
const val ARTISTS_TABLE_NAME = "artists"
const val ARTIST_NAME_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val URL_COLUMN = "url"
const val SOURCE_COLUMN = "source"
const val SOURCE_VALUE = 1
const val DATABASE_NAME = "artists.db"
const val DATABASE_VERSION = 1
const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE_NAME (" +
            "$ARTIST_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_NAME_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$URL_COLUMN string, " +
            "$SOURCE_COLUMN integer)"