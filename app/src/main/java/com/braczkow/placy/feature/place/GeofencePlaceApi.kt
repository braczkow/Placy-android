package com.braczkow.placy.feature.place

import com.braczkow.placy.feature.storage.Storage
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.BroadcastChannel
import java.nio.channels.Channel
import javax.inject.Inject

interface GeofencePlaceApi {
    data class AddPlaceRequest(val name: String, val latLng: LatLng)
    suspend fun addGeofencePlace(addPlaceRequest: AddPlaceRequest)

    data class GeofencePlaceStorage(val places: Map<String, AddPlaceRequest>)
    fun getGeofencePlaceStorage(): GeofencePlaceStorage?
}



class GeofencePlaceApiImpl @Inject constructor(
    private val geofenceApi: GeofenceApi,
    private val storage: Storage
) : GeofencePlaceApi {
    override fun getGeofencePlaceStorage(): GeofencePlaceApi.GeofencePlaceStorage? = storage.load(GeofencePlaceApi.GeofencePlaceStorage::class.java)


    override suspend fun addGeofencePlace(addPlaceRequest: GeofencePlaceApi.AddPlaceRequest) {
        val geofenceRequest = GeofenceApi.CreateGeofenceRequest("iiid", addPlaceRequest.latLng)

        val geofenceResult = geofenceApi.createGeofence(geofenceRequest)

        if (geofenceResult is GeofenceApi.Result.Ok) {
            storeGeofencePlace(geofenceRequest, addPlaceRequest)
        }
    }

    private fun storeGeofencePlace(
        geofenceRequest: GeofenceApi.CreateGeofenceRequest,
        addPlaceRequest: GeofencePlaceApi.AddPlaceRequest
    ) {
        val storageMap = storage.load(GeofencePlaceApi.GeofencePlaceStorage::class.java)?.places ?: mapOf()

        val editable = storageMap.toMutableMap()
        editable[geofenceRequest.id] = addPlaceRequest

        storage.save(GeofencePlaceApi.GeofencePlaceStorage(editable))
    }
}