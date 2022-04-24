package ayds.newyork.songinfo.home.model.repository.external.spotify.tracks

import ayds.newyork.songinfo.home.model.entities.DatePrecision

interface ReleaseDateHelper {
    fun getReleaseDatePrecisionAsEnum(releaseDatePrecision: String):DatePrecision
}

internal class ReleaseDateHelperImpl:ReleaseDateHelper{

    override fun getReleaseDatePrecisionAsEnum(releaseDatePrecision: String):DatePrecision {
        return when(releaseDatePrecision){
            "day" -> DatePrecision.DAY
            "month" -> DatePrecision.MONTH
            "year" -> DatePrecision.YEAR
            else -> throw Exception()
        }
    }
}