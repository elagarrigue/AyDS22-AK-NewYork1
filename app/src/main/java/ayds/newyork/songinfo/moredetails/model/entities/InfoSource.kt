package ayds.newyork.songinfo.moredetails.model.entities

enum class InfoSource (val service: String) {
    NewYorkTimes("New York Times"),
    Wikipedia("Wikipedia"),
    LastFM("Last FM"),
    NoSource("Source Not Found")
}