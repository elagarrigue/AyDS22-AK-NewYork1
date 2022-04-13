package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.SpotifySong
import ayds.newyork.songinfo.home.view.HomeViewInjector.releaseDateMapper
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(releaseDateMapper: ReleaseDateMapper) :
    SongDescriptionHelper {
    private fun getReleaseDateDescription(song: Song): String {
        return releaseDateMapper.getReleaseDatePrecision(song)
    }

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "ReleaseDate: ${getReleaseDateDescription(song)}"
            else -> "Song not found"
        }
    }

}