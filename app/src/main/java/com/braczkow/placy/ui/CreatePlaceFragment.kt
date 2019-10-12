package com.braczkow.placy.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.DisposableOnStop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_create_place.view.*
import javax.inject.Inject

@Subcomponent(modules = [PlaceCreateFragmentModule::class])
interface PlaceCreateFragmentDaggerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): PlaceCreateFragmentDaggerComponent
        fun plus(module: PlaceCreateFragmentModule): Builder
    }

    fun inject(placeCreateFgmt: CreatePlaceFragment)
}

class FgmtView
class Presenter(private val view: FgmtView)

@Module
class PlaceCreateFragmentModule(private val view: FgmtView) {
    @Provides
    fun providePresenter() = Presenter(view)
}


class CreatePlaceFragment : Fragment() {
    @Inject
    lateinit var locationApi: LocationApi

    @Inject
    lateinit var presenter: Presenter

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationReq: LocationUpdatesRequest

    private val TAG = "PLC " + CreatePlaceFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_place, container, false)

        mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            Log.d(TAG, "Got map!")

            map.setOnMapLongClickListener {latLng ->
                Log.d(TAG, "Long click on: ${latLng}")

                moveMarkerTo(latLng)
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

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMS_REQ_CODE)
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d(TAG, "onRequestPermissionsResult: $requestCode $permissions $grantResults")
    }

    override fun onStart() {
        super.onStart()
        val dos = DisposableOnStop(lifecycle)

        locationReq.start()

        dos.add(locationApi
            .locationRx()
            .subscribe {
                moveMarkerTo(LatLng(it.latitude, it.longitude))
            })
    }

    override fun onStop() {
        super.onStop()

        locationReq.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App
            .dagger()
            .placeComponentBuilder()
            .plus(PlaceCreateFragmentModule(FgmtView()))
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)

        locationReq = locationApi.makeLocationUpdatesRequest()
    }

    private fun moveMarkerTo(latLng: LatLng) {
        markerMovementQueue.onNext(
            MarkerMoveRequest(
                latLng,
                DEFAULT_ZOOM
            )
        )
    }

    private val markerMovementQueue = BehaviorSubject.create<MarkerMoveRequest>()

    private data class MarkerMoveRequest(val latLng: LatLng, val zoom : Float)

    companion object {
        private const val DEFAULT_ZOOM = 15.0f
        private const val PERMS_REQ_CODE = 3001
    }
}
