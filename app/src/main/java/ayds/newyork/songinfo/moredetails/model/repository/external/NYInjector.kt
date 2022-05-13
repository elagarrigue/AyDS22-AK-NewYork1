package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.repository.external.infos.*

object NYInjector {
    val nyInfoService: NYInfoService = NYInfoInjector.nyInfoService
}

