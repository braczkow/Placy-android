package com.braczkow.placy.feature.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.braczkow.placy.feature.PermissionApi
import com.braczkow.placy.feature.util.Value
import com.braczkow.placy.feature.util.ValueEmitter
import com.google.android.gms.location.*
import timber.log.Timber
import javax.inject.Inject

interface LocationUpdatesRequest {
    fun start()
    fun stop()
}

interface LocationApi {
    val location: Value<Location>

    fun makeLocationUpdatesRequest() : LocationUpdatesRequest
}

@SuppressLint("MissingPermission")
class LocationApiImpl @Inject constructor(
    val context : Context,
    val permissionApi: PermissionApi
) : LocationApi {
    private val startedRequests = mutableListOf<LocationUpdatesRequest>()

    private val fusedLocationClient: FusedLocationProviderClient

    private val lastLocation: Location? = null

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (permissionApi.hasLocationPermission()) {
            fusedLocationClient.lastLocation?.let { task ->
                task.addOnSuccessListener {location ->
                    location?.let {
                        this.location.emit(it)
                    }
                }
            }
        }

    }

    override val location = ValueEmitter<Location>()


    override fun makeLocationUpdatesRequest(): LocationUpdatesRequest {
        return object : LocationUpdatesRequest {
            override fun start() {
                startedRequests.add(this)
                updateRequest()
            }

            override fun stop() {
                startedRequests.remove(this)
                updateRequest()
            }

        }
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)

            Timber.d( "onLocation: ${p0}")

            p0?.let {
                location.emit(it.lastLocation)
            }
        }
    }

    private fun updateRequest() {
        if (!startedRequests.isEmpty() && permissionApi.hasLocationPermission()) {
            val req = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            fusedLocationClient.requestLocationUpdates(req, callback, Looper.getMainLooper())
        } else {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    companion object {
        val TAG = LocationApi::class.java.simpleName
    }


}