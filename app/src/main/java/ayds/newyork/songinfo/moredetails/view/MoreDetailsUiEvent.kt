package ayds.newyork.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object ShowInfoArticle: MoreDetailsUiEvent()
    object OpenMoreInfoUrl: MoreDetailsUiEvent()
}