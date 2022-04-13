package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.DatePrecision
import ayds.newyork.songinfo.home.model.entities.Song
import java.text.DateFormatSymbols

interface ReleaseDateMapper {
    fun releaseDatePrecision(song: Song): String
}

internal class ReleaseDateMapperImpl : ReleaseDateMapper {
    override fun releaseDatePrecision(song: Song): String =
        when (song.releaseDatePrecision) {
            DatePrecision.DAY -> releaseDatePrecisionDay(song.releaseDate)
            DatePrecision.MONTH -> releaseDatePrecisionMonth(song.releaseDate)
            DatePrecision.YEAR -> releaseDatePrecisionYear(song.releaseDate)
        }
}

private fun releaseDatePrecisionDay(releaseDate: String): String {
    val separator = "/";
    var day = song.releaseDate.split("-")[2]
    var month = song.releaseDate.split("-")[1]
    var year = song.releaseDate.split("-")[0]

    return "$day$separator$month$separator$year"
}

private fun releaseDatePrecisionMonth(releaseDate: String): String {
    val separator = ",";
    var month = DateFormatSymbols().getMonths()[((song.releaseDate.split("-")[1])).toInt()]
    var year = (song.releaseDate.split("-")[0]);
    return "$month$separator $year";

}

private fun releaseDatePrecisionYear(releaseDate: String): String {
    return try {
        val year: Int = song.releaseDate.toInt()
        val leap = if (year.isALeapYear()) "a leap year" else "not a leap year"

        "$year $leap"
    } catch (e: NumberFormatException) {
        "format error"
    }
}

fun Int.isALeapYear() = (this % 4 == 0) && (this % 100 != 0 || this % 400 == 0)

