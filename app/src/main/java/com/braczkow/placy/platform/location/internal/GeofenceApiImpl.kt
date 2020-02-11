package com.braczkow.placy.platform.location.internal

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.braczkow.placy.platform.location.api.GeofenceApi
import com.braczkow.placy.platform.location.api.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeofenceApiImpl @Inject constructor(
    private val context: Context,
    private val someUtils: SomeUtils
) : GeofenceApi {
    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun geofenceEnter(list: List<String>) {
        Timber.d("geofenceEnter: ${list.joinToString()}")
        list.forEach {
            enteredGeofences.add(it)
        }

        channel.offer(enteredGeofences)
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun geofenceExit(list: List<String>) {
        Timber.d("geofenceExit: ${list.joinToString()}")
        list.forEach {
            enteredGeofences.remove(it)
        }

        channel.offer(enteredGeofences)
    }

    val enteredGeofences = mutableSetOf<String>()

    @UseExperimental(ExperimentalCoroutinesApi::class)
    val channel =
        ConflatedBroadcastChannel<Set<String>>()

    @UseExperimental(FlowPreview::class)
    override val currentGeofences: Flow<Set<String>>
        get() = channel.asFlow()

    private val GEOFENCE_BROADCAST_REQ_CODE = 3001
    private val pendingIntent: PendingIntent

    init {
        val intent = Intent(
            context,
            GeofenceBroadcastReceiver::class.java
        )
        pendingIntent = PendingIntent.getBroadcast(
            context,
            GEOFENCE_BROADCAST_REQ_CODE,
            intent,
            0
        )
    }

    override suspend fun createGeofence(createGeofenceRequest: GeofenceApi.CreateGeofenceRequest): GeofenceApi.Result =
        suspendCoroutine { continuation ->
            val client =
                LocationServices.getGeofencingClient(
                    context
                )

            val geofence = Geofence.Builder()
                .setCircularRegion(
                    createGeofenceRequest.latLng.latitude,
                    createGeofenceRequest.latLng.longitude,
                    createGeofenceRequest.radius
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .setRequestId(createGeofenceRequest.id)
                .setExpirationDuration(0)
                .build()

            val request =
                GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build()


            client.addGeofences(request, pendingIntent)
                .addOnSuccessListener {
                    Timber.d("addGeofences success!")
                    continuation.resume(GeofenceApi.Result.Ok)
                }
                .addOnFailureListener {
                    Timber.d("addGeofences failed!")
                    continuation.resume(GeofenceApi.Result.Failed)
                }
        }


}