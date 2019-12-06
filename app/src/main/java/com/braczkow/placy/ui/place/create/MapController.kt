package com.braczkow.placy.ui.place.create

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

interface MapController {
    fun moveMarkerTo(latLng: LatLng)
    fun longClicks(handler: (LatLng) -> Unit)
}

class MapControllerImpl(private val mapFragment: SupportMapFragment) :
    MapController {

    private var longClicksHandler : (LatLng) -> Unit = {}

    override fun longClicks(handler: (LatLng) -> Unit) {
        longClicksHandler = handler
    }

    private val DEFAULT_ZOOM = 15.0f
    private val TAG = "MapCtrl"

    private val markerMoveRequests = mutableListOf<MarkerMoveRequest>()

    private var googleMap: GoogleMap? = null


    init {
        mapFragment.getMapAsync { map ->
            Timber.d( "Got map!")

            googleMap = map

            map.setOnMapLongClickListener {latLng ->
                Timber.d( "Long click on: ${latLng}")
                longClicksHandler(latLng)
            }

            map.setOnMarkerClickListener {
                true
            }

            processMoves()

//            markerMovementQueue
//                .subscribe { mmr ->
//

//                }
        }
    }

    private fun processMoves() {
        googleMap?.let { map ->
            markerMoveRequests.lastOrNull()?.let { mmr ->
                map.clear()

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mmr.latLng, mmr.zoom))
                map.addMarker(MarkerOptions().position(mmr.latLng))
            }

            markerMoveRequests.clear()
        }
    }

    override fun moveMarkerTo(latLng: LatLng) {
        markerMoveRequests.add(MarkerMoveRequest(latLng, DEFAULT_ZOOM))
        processMoves()
    }


    private data class MarkerMoveRequest(val latLng: LatLng, val zoom : Float)
}