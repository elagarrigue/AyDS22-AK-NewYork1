package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Card

data class MoreDetailsUiState(
    val cardList: List<Card>,
    val artistName: String = "",
    val positionSpinner: Int = 2,
)