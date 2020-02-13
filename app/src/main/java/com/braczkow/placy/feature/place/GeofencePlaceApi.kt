package com.braczkow.placy.feature.place

import com.braczkow.platform.location.api.GeofenceApi
import com.braczkow.placy.feature.storage.Storage
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

interface GeofencePlaceApi {
    data class AddPlaceRequest(val name: String, val latLng: LatLng, val radius: Float)
    suspend fun addGeofencePlace(addPlaceRequest: AddPlaceRequest)

    data class GeofencePlaceStorageData(val places: Map<String, AddPlaceRequest>)

    companion object {
        val DEFAULT_RADIUS = 100.0f
    }
}



@InternalCoroutinesApi
class GeofencePlaceApiImpl @Inject constructor(
    private val geofenceApi: GeofenceApi,
    private val storage: Storage
) : GeofencePlaceApi {

    init {

        GlobalScope.launch {
            geofenceApi.currentGeofences
                .collect { enteredPlaces ->

                }
        }

    }

    override suspend fun addGeofencePlace(addPlaceRequest: GeofencePlaceApi.AddPlaceRequest) {
        val geofenceRequest = GeofenceApi.CreateGeofenceRequest(UUID.randomUUID().toString(), addPlaceRequest.latLng, addPlaceRequest.radius)

        val geofenceResult = geofenceApi.createGeofence(geofenceRequest)

        if (geofenceResult is GeofenceApi.Result.Ok) {
            storeGeofencePlace(geofenceRequest, addPlaceRequest)
        }
    }

    private fun storeGeofencePlace(
        geofenceRequest: GeofenceApi.CreateGeofenceRequest,
        addPlaceRequest: GeofencePlaceApi.AddPlaceRequest
    ) {
        val storageMap = storage.load(GeofencePlaceApi.GeofencePlaceStorageData::class.java)?.places ?: mapOf()

        val editable = storageMap.toMutableMap()
        editable[geofenceRequest.id] = addPlaceRequest

        storage.save(GeofencePlaceApi.GeofencePlaceStorageData(editable))
    }

    private fun loadGeofencePlaces(): Map<String, GeofencePlaceApi.AddPlaceRequest> {
        return storage.load(GeofencePlaceApi.GeofencePlaceStorageData::class.java)?.places ?: mapOf()
    }
}