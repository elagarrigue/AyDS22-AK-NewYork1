package ayds.newyork.songinfo.moredetails.model.entities

enum class InfoSource (val service: String) {
    LastFM("Last FM"),
    Wikipedia("Wikipedia"),
    NewYorkTimes("New York Times"),
    NoSource("Source Not Found")
}