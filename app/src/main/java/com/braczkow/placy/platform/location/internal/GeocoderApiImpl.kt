package com.braczkow.placy.platform.location.internal

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.braczkow.placy.platform.location.api.GeocoderApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GeocoderApiImpl @Inject constructor(
    private val context: Context
) : GeocoderApi {

    override suspend fun geocodeLatLng(latLng: LatLng): GeocoderApi.GeocodingResult =
        withContext(Dispatchers.IO) {
            Timber.d("Inside geocodeLatLng: $latLng")
            val result = try {
                val locations = Geocoder(context)
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (locations.size > 0) {
                    GeocoderApi.GeocodingResult(
                        true,
                        formatAddressString(locations[0])
                    )
                } else {
                    GeocoderApi.GeocodingResult(
                        false,
                        GeocoderApi.makeLatLngStr(
                            latLng
                        )
                    )
                }
            } catch (e: Throwable) {
                Timber.d("Geocoder catch: $e")
                GeocoderApi.GeocodingResult(
                    false,
                    GeocoderApi.makeLatLngStr(
                        latLng
                    )
                )
            }

            result
        }

    private fun formatAddressString(address: Address): String {
        return address.toString()
    }
}