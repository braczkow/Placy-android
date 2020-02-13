package com.braczkow.platform.location.api

import android.location.Location

interface LocationUpdatesRequest {
    fun start()
    fun stop()
}

interface LocationApi {
    val location: com.braczkow.base.utils.Value<Location>

    fun makeLocationUpdatesRequest(): LocationUpdatesRequest
}

