package com.braczkow.placy.ui.place.create

import android.view.View
import androidx.lifecycle.Lifecycle
import com.braczkow.placy.feature.location.GeocoderApi
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.*
import com.braczkow.placy.ui.place.MapController
import com.braczkow.placy.ui.util.visible
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_create_place.view.*
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

interface CreatePlaceNavigation {
    sealed class Event {
        data class ProceedWith(val latLng: LatLng) : Event()
    }

    fun navigate(event: Event)
}

class PlacePresenter @Inject constructor(
    private val locationApi: LocationApi,
    private val lifecycle: Lifecycle,
    private val mapController: MapController,
    private val geocoderApi: GeocoderApi,
    private val df: DispatchersFactory,
    private val navigation: CreatePlaceNavigation,
    private val placeView: PlaceView
) {

    private var lastSelectedLocation: LatLng? = null
    private val locationReq: LocationUpdatesRequest
    private val uiScope = CoroutineScope(df.main())

    init {
        placeView.proceedBtnClicks {
            if (lastSelectedLocation != null) {
                Timber.d("Location is set, can proceed")
                navigation.navigate(
                    CreatePlaceNavigation.Event.ProceedWith(
                        lastSelectedLocation!!
                    )
                )
            } else {
                Timber.d("Location not selected, cannot proceed")
            }
        }

        locationReq = locationApi.makeLocationUpdatesRequest()

        DoOnStart(lifecycle) {
            locationReq.start()

            val dos = DisposableOnStop(lifecycle)
            dos.add(locationApi.locationRx()
                .subscribe {
                    val latLng = LatLng(it.latitude, it.longitude)
                    proceedWith(latLng)
                })

            dos.add(mapController
                .longClicks()
                .subscribe {
                    proceedWith(it)
                })
        }


        DoOnStop(lifecycle) {
            uiScope.coroutineContext.cancelChildren()
            locationReq.stop()
        }

    }

    private fun proceedWith(
        it: LatLng
    ) {
        Timber.d("proceedWith: $it")
        lastSelectedLocation = it
        mapController.moveMarkerTo(it)
        startGeocoding(it)
        placeView.showProceedButton()
    }

    private fun startGeocoding(
        latLng: LatLng
    ) {
        placeView.showAddress("Determining the address...")
        uiScope.launch {
            val result = geocoderApi.geocodeLatLng(latLng)
            if (result.isSuccessful) {
                placeView.showAddress(result.result)
            } else {
                placeView.showAddress("Cannot evaluate: ${result.result}")
            }
        }
    }
}
class PlaceView(private val rootView: View) {
    fun showAddress(result: String) {
        rootView.map_address_text.setText(result)
    }

    fun showProceedButton() {
        rootView.map_proceed_frame.visible()
    }

    fun proceedBtnClicks(clicks: () -> Unit) {
        rootView.map_proceed_btn.setOnClickListener { clicks() }
    }

}
