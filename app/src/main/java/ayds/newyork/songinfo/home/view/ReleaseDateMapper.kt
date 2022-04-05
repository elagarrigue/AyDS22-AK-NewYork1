package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song

interface ReleaseDateMapper {
    fun releaseDatePrecision(song: Song): String
}

internal class ReleaseDateMapperImpl : ReleaseDateMapper {
    override fun releaseDatePrecision(song: Song): String =
        when (song.releaseDatePrecision) {
            "day" -> releaseDatePrecisionDay()
            "month" -> releaseDatePrecisionMonth()
            "year" -> releaseDatePrecisionYear(song)
            else -> "error"
        }
}

private fun releaseDatePrecisionDay(): String {
    return "TODO"
}

private fun releaseDatePrecisionMonth(): String {
    return "TODO"
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

