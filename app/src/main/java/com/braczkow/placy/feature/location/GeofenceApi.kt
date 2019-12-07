package com.braczkow.placy.feature.location

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.braczkow.placy.base.App
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
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

    data class CreateGeofenceRequest(val id: String, val latLng: LatLng, val radius: Float)

    suspend fun createGeofence(createGeofenceRequest: CreateGeofenceRequest): Result
    fun geofenceEnter(list: List<String>)
    fun geofenceExit(list: List<String>)
}

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            Timber.e("geofencingEvent.hasError: ${geofencingEvent.errorCode}")
        }

        val geofenceApi = App
            .dagger()
            .geofenceApi()

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER, Geofence.GEOFENCE_TRANSITION_DWELL -> {
                geofenceApi.geofenceEnter(geofencingEvent.triggeringGeofences.map { it.requestId })
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                geofenceApi.geofenceExit(geofencingEvent.triggeringGeofences.map { it.requestId })
            }
        }

    }

}

class GeofenceApiImpl @Inject constructor(
    private val context: Context
) : GeofenceApi {
    override fun geofenceEnter(list: List<String>) {
        Timber.d("geofenceEnter: ${list.joinToString()}")
    }

    override fun geofenceExit(list: List<String>) {
        Timber.d("geofenceExit: ${list.joinToString()}")
    }

    private val GEOFENCE_BROADCAST_REQ_CODE = 3001
    private val pendingIntent: PendingIntent

    init {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            GEOFENCE_BROADCAST_REQ_CODE,
            intent,
            0
        )
    }

    override suspend fun createGeofence(createGeofenceRequest: GeofenceApi.CreateGeofenceRequest): GeofenceApi.Result =
        suspendCoroutine { continuation ->
            val client = LocationServices.getGeofencingClient(context)

            val geofence = Geofence.Builder()
                .setCircularRegion(
                    createGeofenceRequest.latLng.latitude,
                    createGeofenceRequest.latLng.longitude,
                    createGeofenceRequest.radius
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .setRequestId(createGeofenceRequest.id)
                .setExpirationDuration(0)
                .build()

            val request = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()


            client.addGeofences(request, pendingIntent)
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

