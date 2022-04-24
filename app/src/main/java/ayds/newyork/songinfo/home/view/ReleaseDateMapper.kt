package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.DatePrecision
import ayds.newyork.songinfo.home.model.entities.Song
import java.text.DateFormatSymbols

interface ReleaseDateMapper {
    fun getReleaseDatePrecision(song: Song): String
}

internal class ReleaseDateMapperImpl : ReleaseDateMapper {
    override fun getReleaseDatePrecision(song: Song): String =
        when (song.releaseDatePrecision) {
            DatePrecision.DAY -> getReleaseDatePrecisionDay(song.releaseDate)
            DatePrecision.MONTH -> getReleaseDatePrecisionMonth(song.releaseDate)
            DatePrecision.YEAR -> getReleaseDatePrecisionYear(song.releaseDate)
        }
}

private fun getReleaseDatePrecisionDay(releaseDate: String): String {
    val separator = "/"
    val releaseDateArray = releaseDate.split("-").toTypedArray()
    val day = releaseDateArray[2]
    val month = releaseDateArray[1]
    val year = releaseDateArray[0]

    return "$day$separator$month$separator$year"
}

private fun getReleaseDatePrecisionMonth(releaseDate: String): String {
    val separator = ","
    val releaseDateArray= releaseDate.split("-").toTypedArray()
    val month = DateFormatSymbols().months[((releaseDateArray[1])).toInt()]
    val year = releaseDateArray[0]
    return "$month$separator $year"

}

private fun getReleaseDatePrecisionYear(releaseDate: String): String {
    return try {
        val year: Int = releaseDate.toInt()
        val leap = if (year.isALeapYear()) "a leap year" else "not a leap year"

        "$year $leap"
    } catch (e: NumberFormatException) {
        throw e
    }
}

fun Int.isALeapYear() = (this % 4 == 0) && (this % 100 != 0 || this % 400 == 0)

