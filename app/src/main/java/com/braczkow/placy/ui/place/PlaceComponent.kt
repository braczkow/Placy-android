package com.braczkow.placy.ui.place

import android.view.View
import androidx.lifecycle.Lifecycle
import com.braczkow.placy.feature.location.GeocoderApi
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationUpdatesRequest
import com.braczkow.placy.feature.util.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_create_place.view.*
import javax.inject.Inject


class PlacePresenter @Inject constructor(
    private val locationApi: LocationApi,
    private val lifecycle: Lifecycle,
    private val mapController: MapController,
    private val geocoderApi: GeocoderApi,
    private val sf: SchedulersFactory,
    private val placeView: PlaceView) {

    private val locationReq: LocationUpdatesRequest

    init {
        locationReq = locationApi.makeLocationUpdatesRequest()

        DoOnStart(lifecycle) {
            locationReq.start()

            val dos = DisposableOnStop(lifecycle)
            dos.add(locationApi.locationRx()
                .subscribe {
                    val latLng = LatLng(it.latitude, it.longitude)
                    proceedWith(latLng, dos)
                })

            dos.add(mapController
                .longClicks()
                .subscribe {
                    proceedWith(it, dos)
                })
        }


        DoOnStop(lifecycle) {
            locationReq.stop()
        }

    }

    private fun proceedWith(
        it: LatLng,
        dos: DisposableOnStop
    ) {
        mapController.moveMarkerTo(it)
        startGeocoding(it, dos)
        placeView.showProceedButton()
    }

    private fun startGeocoding(
        latLng: LatLng,
        dos: DisposableOnStop
    ) {
        dos.add(geocoderApi
            .getFromLatLng(latLng)
            .observeOn(sf.mainThread())
            .subscribe { success, error ->
                if (success?.isSuccessful == true) {
                    placeView.showAddress(success.result ?: "Default")
                }
            })
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
