package com.braczkow.placy.platform.location.api

import android.location.Location
import com.braczkow.placy.feature.util.Value

interface LocationUpdatesRequest {
    fun start()
    fun stop()
}

interface LocationApi {
    val location: Value<Location>

    fun makeLocationUpdatesRequest(): LocationUpdatesRequest
}

