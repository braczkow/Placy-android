package com.braczkow.placy.feature.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.braczkow.placy.feature.util.SchedulersFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject



interface GeocoderApi {
    data class GeocodingResult(val isSuccessful : Boolean, val result: String)
    suspend fun geocodeLatLng(latLng: LatLng): GeocodingResult

    companion object {
        fun makeLatLngStr(latLng: LatLng): String {
            return String.format(Locale.US, "(%.6f, %.6f)", latLng.latitude, latLng.longitude)
        }
    }
}

class GeocoderApiImpl @Inject constructor(
    private val context: Context,
    private val sf: SchedulersFactory
) : GeocoderApi {

    override suspend fun geocodeLatLng(latLng: LatLng): GeocoderApi.GeocodingResult = withContext(Dispatchers.IO) {
        Timber.d("Inside geocodeLatLng: $latLng")
        val result = try {
            val locations = Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (locations.size > 0) {
                GeocoderApi.GeocodingResult(true, formatAddressString(locations[0]))
            } else {
                GeocoderApi.GeocodingResult(
                    false,
                    GeocoderApi.makeLatLngStr(latLng)
                )
            }
        } catch (e : Throwable) {
            Timber.d("Geocoder catch: $e")
            GeocoderApi.GeocodingResult(
                false,
                GeocoderApi.makeLatLngStr(latLng))
        }

        result
    }

    private fun formatAddressString(address: Address): String {
        return address.toString()
    }
}