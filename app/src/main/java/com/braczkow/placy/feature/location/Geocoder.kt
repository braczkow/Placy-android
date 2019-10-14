package com.braczkow.placy.feature.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.braczkow.placy.feature.util.SchedulersFactory
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import java.util.*
import javax.inject.Inject



interface GeocoderApi {
    data class GeocodingResult(val isSuccessful : Boolean, val result: String?)
    fun getFromLatLng(latLng: LatLng): Single<GeocodingResult>

    companion object {
        fun makeLatLngStr(latLng: LatLng): String {
            return String.format(Locale.US, "%.6f, %.6f", latLng.latitude, latLng.longitude)
        }
    }
}

class GeocoderApiImpl @Inject constructor(
    private val context: Context,
    private val sf: SchedulersFactory
) : GeocoderApi {
    override fun getFromLatLng(latLng: LatLng): Single<GeocoderApi.GeocodingResult> {
        return Single.fromCallable {
            try {
                val locations = Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, 1)
                return@fromCallable if (locations.size > 0) {
                    GeocoderApi.GeocodingResult(true, formatAddressString(locations[0]))
                } else {
                    GeocoderApi.GeocodingResult(
                        false,
                        GeocoderApi.makeLatLngStr(latLng)
                    )
                }
            } catch (e: Exception) {
                //Timber.d("Geocoder exception: $e")
                return@fromCallable GeocoderApi.GeocodingResult(
                    false,
                    GeocoderApi.makeLatLngStr(latLng)
                )
            }
        }.subscribeOn(sf.io())
    }

    private fun formatAddressString(address: Address): String? {
        var result: String? = null

        val maxLineIndex = address.maxAddressLineIndex
        val sb = StringBuilder()
        for (lineIndex in 0..maxLineIndex) {
            if (lineIndex != 0) {
                sb.append(", ")
            }
            sb.append(address.getAddressLine(lineIndex))
        }
        val addressStr = sb.toString().trim { it <= ' ' }
        if (addressStr.isNotEmpty()) {
            result = addressStr
        }

        return result
    }
}