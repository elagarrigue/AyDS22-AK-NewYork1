package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song

interface ReleaseDateMapper {
    fun ReleaseDatePrecision(song: Song): String
}

internal class ReleaseDateMapperImpl : ReleaseDateMapper {
    override fun ReleaseDatePrecision(song: Song): String {
        return "TODO"
    }

    private fun ReleaseDatePrecisionDay(): String {
        return "TODO"
    }

    private fun ReleaseDatePrecisionMonth(): String {
        return "TODO"
    }

    private fun ReleaseDatePrecisionYear(): String {
        return "TODO"
    }

}