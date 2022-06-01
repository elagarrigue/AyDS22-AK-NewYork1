package ayds.newyork.songinfo.moredetails.model.repository.external.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface Proxy {
    fun getCard(artistName: String): Card?
}