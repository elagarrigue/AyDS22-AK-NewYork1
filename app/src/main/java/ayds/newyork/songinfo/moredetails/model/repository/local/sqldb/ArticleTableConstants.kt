package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

const val ARTICLE_ID_COLUMN = "id"
const val ARTICLES_TABLE_NAME = "articles"
const val ARTIST_NAME_COLUMN = "artist"
const val ARTICLE_DESC = "artist DESC"
const val ARTICLE_INFO_COLUMN = "info"
const val ARTICLE_URL_COLUMN = "url"
const val ARTICLE_SOURCE_COLUMN = "source"
const val SOURCE_VALUE = 1
const val DATABASE_NAME = "articles.db"
const val DATABASE_VERSION = 1
const val createArtistsTableQuery: String =
    "create table $ARTICLES_TABLE_NAME (" +
            "$ARTICLE_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_NAME_COLUMN string, " +
            "$ARTICLE_INFO_COLUMN string, " +
            "$ARTICLE_URL_COLUMN string, " +
            "$ARTICLE_SOURCE_COLUMN integer)"