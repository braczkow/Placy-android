package com.braczkow.placy.feature.place.impl

import com.braczkow.placy.feature.place.GeofencePlaceApi
import com.braczkow.placy.feature.storage.Storage
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface GeofencePlaceStorage {
    fun store(
        id: String,
        addPlaceRequest: GeofencePlaceApi.AddPlaceRequest
    )

    fun stream(): Observable<GeofencePlaceApi.GeofencePlaceStorageData>
}

class GeofencePlaceStorageImpl @Inject constructor(
    private val storage: Storage
) : GeofencePlaceStorage {

    private val publisher = BehaviorSubject.create<GeofencePlaceApi.GeofencePlaceStorageData>()

    init {
        publisher.onNext(
            storage.load(GeofencePlaceApi.GeofencePlaceStorageData::class.java)
                ?: GeofencePlaceApi.GeofencePlaceStorageData(mapOf())
        )
    }

    override fun stream(): Observable<GeofencePlaceApi.GeofencePlaceStorageData> = publisher

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
        publisher.onNext(updated)
    }
}