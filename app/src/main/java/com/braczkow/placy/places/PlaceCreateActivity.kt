package com.braczkow.placy.places

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.places.di.SomeApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_place_create.*
import javax.inject.Inject


class PlaceCreateActivity : AppCompatActivity() {

    @Inject
    lateinit var someApi: SomeApi

    private lateinit var mapFragment: SupportMapFragment

    private val TAG = "PLC " + PlaceCreateActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        App.dagger().placeComponentBuilder().build().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_create)

        this.place_create_api_txt.setText(someApi.haveFun())

        mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            Log.d(TAG, "Got map!")

            map.setOnMapLongClickListener {latLng ->
                Log.d(TAG, "Long click on: ${latLng}")

                moveMarkerTo(latLng)
            }

            markerMovementQueue
                .subscribe { mmr ->
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mmr.latLng, mmr.zoom))
                }
        }
    }

    private fun moveMarkerTo(latLng: LatLng) {
        markerMovementQueue.onNext(MarkerMoveRequest(latLng, DEFAULT_ZOOM))
    }

    private val markerMovementQueue = BehaviorSubject.create<MarkerMoveRequest>()

    private data class MarkerMoveRequest(val latLng: LatLng, val zoom : Float)

    companion object {
        private const val DEFAULT_ZOOM = 15.0f
    }

}
