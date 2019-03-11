package com.braczkow.placy.places

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.braczkow.placy.core.PermissionApi
import com.braczkow.placy.core.PermissionState
import com.google.android.gms.location.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface LocationUpdatesRequest {
    fun start()
    fun stop()
}

interface LocationApi {
    fun locationRx() : Observable<Location>
    fun makeLocationUpdatesRequest() : LocationUpdatesRequest
}

@SuppressLint("MissingPermission")
class LocationApiImpl @Inject constructor(
    val context : Context,
    val permissionApi: PermissionApi
) : LocationApi {
    private val locationPublisher = BehaviorSubject.create<Location>()
    private val startedRequests = mutableListOf<LocationUpdatesRequest>()

    private val fusedLocationClient: FusedLocationProviderClient


    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (permissionApi.hasLocationPermission()) {
            fusedLocationClient.lastLocation?.let { task ->
                task.addOnSuccessListener {location ->
                    locationPublisher.onNext(location)
                }
            }
        }

    }

    override fun locationRx() = locationPublisher

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

            Log.d(TAG, "onLocation: ${p0}")

            p0?.let {
                locationPublisher.onNext(it.lastLocation)
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