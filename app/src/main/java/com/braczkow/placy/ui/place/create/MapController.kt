package com.braczkow.placy.ui.place.create

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

interface MapController {
    fun moveMarkerTo(latLng: LatLng)
    fun longClicks(): Observable<LatLng>
}

class MapControllerImpl(private val mapFragment: SupportMapFragment) :
    MapController {
    private val DEFAULT_ZOOM = 15.0f
    private val TAG = "MapCtrl"

    private val longClicksPublisher = PublishSubject.create<LatLng>()

    init {
        mapFragment.getMapAsync { map ->
            Timber.d( "Got map!")

            map.setOnMapLongClickListener {latLng ->
                Timber.d( "Long click on: ${latLng}")
                longClicksPublisher.onNext(latLng)
            }

            map.setOnMarkerClickListener {
                true
            }

            markerMovementQueue
                .subscribe { mmr ->

                    map.clear()

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mmr.latLng, mmr.zoom))
                    map.addMarker(MarkerOptions().position(mmr.latLng))
                }
        }
    }

    override fun longClicks(): Observable<LatLng> = longClicksPublisher
    override fun moveMarkerTo(latLng: LatLng) {
        markerMovementQueue.onNext(
            MarkerMoveRequest(
                latLng,
                DEFAULT_ZOOM
            )
        )
    }

    private val markerMovementQueue = BehaviorSubject.create<MarkerMoveRequest>()

    private data class MarkerMoveRequest(val latLng: LatLng, val zoom : Float)
}