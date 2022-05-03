package ayds.newyork.songinfo.moredetails.model.repository.external.infos

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.external.NYInfoService
import retrofit2.Response

internal class NYInfoServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToInfoResolver: NYTimesToInfoResolver,
):NYInfoService {


    override fun getArtistInfo(artistName: String): ArtistInfo? {
        val callResponse = getArtistInfoFromService(artistName)
        return nyTimesToInfoResolver.getArtistInfoFromExternalData(callResponse.body())
    }

    private fun getArtistInfoFromService(query: String): Response<String> {
        return nyTimesAPI.getArtistInfo(query)
            .execute()
    }
}
