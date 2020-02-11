package com.braczkow.placy.platform.location.api

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.braczkow.placy.app.App
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface GeofenceApi {
    sealed class Result {
        object Ok : Result()
        object Failed : Result()
    }

    data class CreateGeofenceRequest(val id: String, val latLng: LatLng, val radius: Float)
    data class GeofenceState(val id: String, val entered: Boolean)

    suspend fun createGeofence(createGeofenceRequest: CreateGeofenceRequest): Result
    fun geofenceEnter(list: List<String>)
    fun geofenceExit(list: List<String>)

    val currentGeofences: Flow<Set<String>>
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

