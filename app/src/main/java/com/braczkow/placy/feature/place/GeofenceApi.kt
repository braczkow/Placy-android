package com.braczkow.placy.feature.place

import android.content.Context
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface GeofenceApi {
    sealed class Result {
        object Ok : Result()
        object Failed : Result()
    }

    data class CreateGeofenceRequest(val id: String, val latLng: LatLng)

    suspend fun createGeofence(createGeofenceRequest: CreateGeofenceRequest): Result
}

class GeofenceApiImpl @Inject constructor(
    private val context: Context
) : GeofenceApi {

    private val DEFAULT_RADIUS = 100.0f

    override suspend fun createGeofence(createGeofenceRequest: GeofenceApi.CreateGeofenceRequest): GeofenceApi.Result =
        suspendCoroutine { continuation ->
            val client = LocationServices.getGeofencingClient(context)

            val geofence = Geofence.Builder()
                .setCircularRegion(
                    createGeofenceRequest.latLng.latitude,
                    createGeofenceRequest.latLng.longitude,
                    DEFAULT_RADIUS
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .setRequestId(createGeofenceRequest.id)
                .setExpirationDuration(0)
                .build()

            val request = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()


            client.addGeofences(request, null)
                .addOnSuccessListener {
                    Timber.d("addGeofences success!")
                    continuation.resume(GeofenceApi.Result.Ok)
                }
                .addOnFailureListener {
                    Timber.d("addGeofences failed!")
                    continuation.resume(GeofenceApi.Result.Failed)
                }
        }


}

