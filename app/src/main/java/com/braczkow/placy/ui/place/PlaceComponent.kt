package com.braczkow.placy.ui.place

import android.view.View
import androidx.lifecycle.Lifecycle
import com.braczkow.placy.feature.location.GeocoderApi
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_create_place.view.*
import kotlinx.coroutines.*
import javax.inject.Inject


class PlacePresenter @Inject constructor(
    private val locationApi: LocationApi,
    private val lifecycle: Lifecycle,
    private val mapController: MapController,
    private val geocoderApi: GeocoderApi,
    private val df: DispatchersFactory,
    private val placeView: PlaceView) {

    private val locationReq: LocationUpdatesRequest
    private val uiScope = CoroutineScope(df.main())

    init {
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
        mapController.moveMarkerTo(it)
        startGeocoding(it)
        placeView.showProceedButton()
    }

    private fun startGeocoding(
        latLng: LatLng
    ) {
        uiScope.launch {
            val result = geocoderApi.geocodeLatLng(latLng)
            if (result.isSuccessful) {
                placeView.showAddress(result.result)
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

}
