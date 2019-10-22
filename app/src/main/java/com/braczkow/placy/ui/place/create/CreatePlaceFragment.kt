package com.braczkow.placy.ui.place.create


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.braczkow.placy.R
import com.braczkow.placy.base.App
import com.braczkow.placy.ui.place.CreatePlaceFragmentDirections
import com.braczkow.placy.ui.place.MapController
import com.braczkow.placy.ui.place.MapControllerImpl
import com.google.android.gms.maps.SupportMapFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import timber.log.Timber
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
        val navigation: CreatePlaceNavigation,
        val placeView: PlaceView
    ) {

        @Provides
        fun provideLifecycle() = lifecycle

        @Provides
        fun provideMapController() = mapController

        @Provides
        fun provideNavigation() = navigation

        @Provides
        fun provideView() = placeView
    }

    @Inject
    lateinit var placePresenter: PlacePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val view =  inflater.inflate(R.layout.fragment_create_place, container, false)
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        val mapController = MapControllerImpl(mapFragment)

        App
            .dagger()
            .placeComponentBuilder()
            .plus(
                DaggerModule(
                    lifecycle, mapController, createNavigation(),
                    PlaceView(view)
                )
            )
            .build()
            .inject(this)

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMS_REQ_CODE
            )
        }

        return view
    }

    private fun createNavigation(): CreatePlaceNavigation {
        return object : CreatePlaceNavigation {
            override fun navigate(event: CreatePlaceNavigation.Event) {
                if (event is CreatePlaceNavigation.Event.ProceedWith) {
                    val action =
                        CreatePlaceFragmentDirections.actionCreatePlaceFragmentToSetNameFragment(
                            event.latLng
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Timber.d( "onRequestPermissionsResult: $requestCode $permissions $grantResults")
    }


    companion object {
        private const val PERMS_REQ_CODE = 3001
    }
}
