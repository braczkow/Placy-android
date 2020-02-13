package com.braczkow.placy.ui.place.create

import androidx.lifecycle.Lifecycle
import com.braczkow.platform.location.api.GeocoderApi
import com.braczkow.platform.location.api.LocationApi
import com.braczkow.platform.location.api.LocationUpdatesRequest
import com.braczkow.base.utils.DispatchersFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CreatePlacePresenter @Inject constructor(
    private val locationApi: LocationApi,
    private val lifecycle: Lifecycle,
    private val mapController: MapController,
    private val geocoderApi: GeocoderApi,
    private val df: DispatchersFactory,
    private val navigation: CreatePlaceNavigation,
    private val createPlaceView: CreatePlaceView
) {

    private var lastSelectedLocation: LatLng? = null
    private val locationReq: LocationUpdatesRequest
    private val uiScope = CoroutineScope(df.main())

    init {
        createPlaceView.proceedBtnClicks {
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

        com.braczkow.base.utils.DoOnStart(lifecycle) {
            locationReq.start()

            locationApi.location.subscribe {
                val latLng = LatLng(it.latitude, it.longitude)
                proceedWith(latLng)
            }.also {
                com.braczkow.base.utils.CancelOnStop(lifecycle, it)
            }

            mapController
                .longClicks {
                    proceedWith(it)
                }
        }


        com.braczkow.base.utils.DoOnStop(lifecycle) {
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
        createPlaceView.showProceedButton()
    }

    private fun startGeocoding(
        latLng: LatLng
    ) {
        createPlaceView.showAddress("Determining the address...")
        uiScope.launch {
            val result = geocoderApi.geocodeLatLng(latLng)
            if (result.isSuccessful) {
                createPlaceView.showAddress(result.result)
            } else {
                createPlaceView.showAddress("Cannot evaluate: ${result.result}")
            }
        }
    }
}