package com.braczkow.placy.ui.place.create

import androidx.lifecycle.Lifecycle
import com.braczkow.placy.feature.location.GeocoderApi
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.DispatchersFactory
import com.braczkow.placy.feature.util.DisposableOnStop
import com.braczkow.placy.feature.util.DoOnStart
import com.braczkow.placy.feature.util.DoOnStop
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