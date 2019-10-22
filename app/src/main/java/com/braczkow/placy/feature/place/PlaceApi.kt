package com.braczkow.placy.feature.place

import android.content.Context
import android.provider.SyncStateContract
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface PlaceApi {
    sealed class Result {
        object Ok : Result()
        object Failed : Result()
    }

    data class PlaceData(val name: String, val latLng: LatLng)

    suspend fun createPlace(placeData: PlaceData): Result
}

class PlaceApiImpl @Inject constructor(
    private val context: Context
) : PlaceApi {

    private val DEFAULT_RADIUS = 100.0f

    override suspend fun createPlace(placeData: PlaceApi.PlaceData): PlaceApi.Result =
        suspendCoroutine { continuation ->
            val client = LocationServices.getGeofencingClient(context)

            val geofence = Geofence.Builder()
                .setCircularRegion(
                    placeData.latLng.latitude,
                    placeData.latLng.longitude,
                    DEFAULT_RADIUS
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .setRequestId(placeData.name)
                .setExpirationDuration(0)
                .build()

            val request = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()


            client.addGeofences(request, null)
                .addOnSuccessListener {
                    Timber.d("addGeofences success!")
                    continuation.resume(PlaceApi.Result.Ok)
                }
                .addOnFailureListener {
                    Timber.d("addGeofences failed!")
                    continuation.resume(PlaceApi.Result.Failed)
                }
        }


}

