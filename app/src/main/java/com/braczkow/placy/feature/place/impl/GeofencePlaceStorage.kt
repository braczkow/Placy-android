package com.braczkow.placy.feature.place.impl

import com.braczkow.placy.feature.place.GeofencePlaceApi
import com.braczkow.placy.feature.storage.Storage
import javax.inject.Inject

interface GeofencePlaceStorage {
    fun store(
        id: String,
        addPlaceRequest: GeofencePlaceApi.AddPlaceRequest
    )

}

class GeofencePlaceStorageImpl @Inject constructor(
    private val storage: Storage
) : GeofencePlaceStorage {


    init {

    }


    override fun store(
        id: String,
        addPlaceRequest: GeofencePlaceApi.AddPlaceRequest
    ) {
        val storageMap =
            storage.load(GeofencePlaceApi.GeofencePlaceStorageData::class.java)?.places ?: mapOf()

        val editable = storageMap.toMutableMap()
        editable[id] = addPlaceRequest

        val updated = GeofencePlaceApi.GeofencePlaceStorageData(editable)
        storage.save(updated)

//        publisher.onNext(updated)
    }
}