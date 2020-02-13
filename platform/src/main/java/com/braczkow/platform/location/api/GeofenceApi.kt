package com.braczkow.platform.location.api

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface GeofenceApi {
    sealed class Result {
        object Ok : Result()
        object Failed : Result()
    }

    data class CreateGeofenceRequest(val id: String, val latLng: LatLng, val radius: Float)
    data class GeofenceState(val id: String, val entered: Boolean)

    suspend fun createGeofence(createGeofenceRequest: CreateGeofenceRequest): Result

    val currentGeofences: Flow<Set<String>>
}

