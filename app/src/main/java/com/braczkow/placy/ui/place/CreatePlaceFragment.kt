package com.braczkow.placy.ui.place


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.DisposableOnStop
import com.google.android.gms.maps.SupportMapFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject


class CreatePlaceFragment : Fragment() {
    @Subcomponent(modules = [DaggerModule::class])
    interface DaggerComponent {
        @Subcomponent.Builder
        interface Builder {
            fun build(): DaggerComponent
            fun plus(module: DaggerModule): Builder
        }

        fun inject(placeCreateFgmt: CreatePlaceFragment)
    }

    @Module
    class DaggerModule(
        val lifecycle: Lifecycle,
        val mapController: MapController,
        val placeView: PlaceView) {
        @Provides
        fun provideLifecycle() = lifecycle

        @Provides
        fun provideMapController() = mapController

        @Provides
        fun provideView() = placeView
    }

    @Inject
    lateinit var locationApi: LocationApi

    @Inject
    lateinit var placePresenter: PlacePresenter



    private val TAG = "PLC " + CreatePlaceFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_place, container, false)
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        val mapController = MapControllerImpl(mapFragment)

        App
            .dagger()
            .placeComponentBuilder()
            .plus(DaggerModule(lifecycle, mapController, PlaceView(view)))
            .build()
            .inject(this)




        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMS_REQ_CODE
            )
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


    companion object {
        private const val PERMS_REQ_CODE = 3001
    }
}
