package ayds.newyork.songinfo.moredetails.view

data class MoreDetailsUiState(
    val artistName: String = "",
    val artistUrl: String = "",
    val artistInfo: String = "",
    val imageUrl: String = NYTIMES_IMAGE,
) {
    companion object {
        const val NYTIMES_IMAGE =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    }
}