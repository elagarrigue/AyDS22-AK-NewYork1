package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
    fun getReleaseDateDescription(song:Song =EmptySong): String
}

internal class SongDescriptionHelperImpl (releaseDateMapper: ReleaseDateMapper): SongDescriptionHelper {
    override fun getReleaseDateDescription(song: Song): String {
        var releaseDateDescription=""
        releaseDateDescription = if (song.releaseDatePrecision=="year"){
            (song.releaseDate.split("-").first())
        } else if (song.releaseDatePrecision=="month"){
            DateFormatSymbols().getMonths()[((song.releaseDate.split("-")[1])).toInt()]+","+(song.releaseDate.split("-").first())
        } else{
            (song.releaseDate.split("-")[2])+"/"+(song.releaseDate.split("-")[1])+"/"+(song.releaseDate.split("-").first())
        }
        return releaseDateDescription
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