package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard

data class MoreDetailsUiState(
    val cardList: List<Card> = listOf(),
    val artistName: String = "",
    val positionSpinner: Int = 0,
) {
    fun getCurrentCard(): Card {
        return if (positionSpinner>(cardList.size-1))
            EmptyCard
        else
            cardList[positionSpinner]
    }


    companion object {
        const val LOGO_NOT_FOUND_URL =
            "https://bitsofco.de/content/images/2018/12/broken-1.png"
    }
}

