package com.braczkow.placy.platform.location.api

import com.google.android.gms.maps.model.LatLng
import java.util.*


interface GeocoderApi {
    data class GeocodingResult(val isSuccessful : Boolean, val result: String)
    suspend fun geocodeLatLng(latLng: LatLng): GeocodingResult

    companion object {
        fun makeLatLngStr(latLng: LatLng): String {
            return String.format(Locale.US, "(%.6f, %.6f)", latLng.latitude, latLng.longitude)
        }
    }
}

