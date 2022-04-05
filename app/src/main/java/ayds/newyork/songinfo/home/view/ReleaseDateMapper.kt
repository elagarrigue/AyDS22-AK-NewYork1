package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song
import java.text.DateFormatSymbols

interface ReleaseDateMapper {
    fun releaseDatePrecision(song: Song): String
}

internal class ReleaseDateMapperImpl : ReleaseDateMapper {
    override fun releaseDatePrecision(song: Song): String =
        when (song.releaseDatePrecision) {
            "day" -> releaseDatePrecisionDay(song)
            "month" -> releaseDatePrecisionMonth(song)
            "year" -> releaseDatePrecisionYear(song)
            else -> "error"
        }
}

private fun releaseDatePrecisionDay(song: Song): String {
    return (song.releaseDate.split("-")[2])+"/"+(song.releaseDate.split("-")[1])+"/"+(song.releaseDate.split("-").first())
}

private fun releaseDatePrecisionMonth(song: Song): String {
    return DateFormatSymbols().getMonths()[((song.releaseDate.split("-")[1])).toInt()]+", "+(song.releaseDate.split("-").first())
}

private fun releaseDatePrecisionYear(song: Song): String {
    return try {
        val year: Int = song.releaseDate.toInt()
        val leap = if (year.isALeapYear()) "a leap year" else "not a leap year"

        "$year $leap"
    } catch (e: NumberFormatException) {
        "format error"
    }
}

fun Int.isALeapYear() = (this % 4 == 0) && (this % 100 != 0 || this % 400 == 0)

