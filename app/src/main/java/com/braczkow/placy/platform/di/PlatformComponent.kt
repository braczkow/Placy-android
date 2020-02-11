package com.braczkow.placy.platform.di

import com.braczkow.placy.platform.location.api.GeocoderApi
import com.braczkow.placy.platform.location.api.GeofenceApi
import com.braczkow.placy.platform.location.api.LocationApi
import com.braczkow.placy.platform.network.api.BluetoothApi
import dagger.Component


@PlatformScope
@Component(
    modules = [
        PlatformModule::class
    ]
)
interface PlatformComponent {
    @Component.Builder
    interface Builder {
        fun build(): PlatformComponent
        fun plus(platformModule: PlatformModule): Builder
    }

    fun geofenceApi(): GeofenceApi
    fun locationApi(): LocationApi
    fun geocoderApi(): GeocoderApi
    fun bluetoothApi(): BluetoothApi
}