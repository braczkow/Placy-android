package com.braczkow.placy.feature

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import javax.inject.Inject

enum class PermissionState {
    GRANTED,
    DENIED_ONCE,
    DENIED_PERMANENT
}

interface PermissionApi {
    fun hasLocationPermission() : Boolean
}

class PermissionApiImpl @Inject constructor(val context: Context) :
    PermissionApi {
    override fun hasLocationPermission(): Boolean {
        //todo get from storage
        return when (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> true
            else -> false
        }
    }

}