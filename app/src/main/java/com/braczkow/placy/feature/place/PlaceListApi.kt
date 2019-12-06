package com.braczkow.placy.feature.place

import com.braczkow.placy.feature.place.impl.GeofencePlaceStorage
//import io.reactivex.Observable
import javax.inject.Inject

interface PlaceListApi {
    data class PlaceListEntry(val name: String, val isEntered: Boolean)

//    fun getPlaceList(): Observable<List<PlaceListEntry>>
}

class PlaceListApiImpl @Inject constructor(
    private val geofencePlaceStorage: GeofencePlaceStorage
) : PlaceListApi {
//    override fun getPlaceList(): Observable<List<PlaceListApi.PlaceListEntry>> {
//        return geofencePlaceStorage
//            .stream()
//            .map {
//                it.places.map {
//                    PlaceListApi.PlaceListEntry(it.value.name, false)
//                }
//            }
//    }

}