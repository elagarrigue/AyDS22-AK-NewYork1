package ayds.newyork.songinfo.moredetails.model.repository.external.infos

import retrofit2.Call
import retrofit2.http.*

internal interface NYTimesAPI {
    @GET("articlesearch.json?api-key=fFnIAXXz8s8aJ4dB8CVOJl0Um2P96Zyx")
    fun getArtistInfo(
        @Query("q") artist: String?
    ): Call<String>
}