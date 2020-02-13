package com.braczkow.placy.ui.place.set_name

import androidx.lifecycle.Lifecycle
import com.braczkow.platform.location.api.GeofenceApi
import com.braczkow.placy.feature.place.GeofencePlaceApi
import com.braczkow.base.utils.DispatchersFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SetNamePresenter @Inject constructor(
    private val geofenceApi: GeofenceApi,
    private val df: DispatchersFactory,
    private val lifecycle: Lifecycle,
    private val view: SetNameView,
    private val navigation: SetNameNavigation,
    private val latLng: LatLng
) {

    private val uiScope = CoroutineScope(df.main())

    init {

        com.braczkow.base.utils.DoOnStop(lifecycle) {
            uiScope.coroutineContext.cancelChildren()
        }


        view.saveClicks {
            if (view.getName().isEmpty()) {
                Timber.d("Empty name")
                return@saveClicks
            }

            uiScope.launch {
                val result = geofenceApi.createGeofence(
                    GeofenceApi.CreateGeofenceRequest(
                        view.getName(),
                        latLng,
                        GeofencePlaceApi.DEFAULT_RADIUS
                    )
                )
                Timber.d("Result is $result")

                navigation.navigate(SetNameNavigation.Event.Finish)
            }
        }
    }

}