package com.braczkow.placy.platform.location.internal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.braczkow.placy.platform.location.api.LocationApi
import com.braczkow.placy.platform.location.api.LocationUpdatesRequest
import com.braczkow.placy.feature.util.ValueEmitter
import com.google.android.gms.location.*
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationApiImpl @Inject constructor(
    val context: Context
) : LocationApi {
    private val startedRequests = mutableListOf<LocationUpdatesRequest>()

    private val fusedLocationClient: FusedLocationProviderClient

    private val lastLocation: Location? = null

    init {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(
                context
            )

        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation?.let { task ->
                task.addOnSuccessListener { location ->
                    location?.let {
                        this.location.emit(it)
                    }
                }
            }
        }

    }

    override val location =
        ValueEmitter<Location>()


    override fun makeLocationUpdatesRequest(): LocationUpdatesRequest {
        return object :
            LocationUpdatesRequest {
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

            Timber.d("onLocation: ${p0}")

            p0?.let {
                location.emit(it.lastLocation)
            }
        }
    }

    private fun updateRequest() {
        if (!startedRequests.isEmpty() && hasLocationPermission()) {
            val req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            fusedLocationClient.requestLocationUpdates(req, callback,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}