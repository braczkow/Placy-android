package com.braczkow.platform.di

import com.braczkow.platform.location.api.GeocoderApi
import com.braczkow.platform.location.api.GeofenceApi
import com.braczkow.platform.location.api.LocationApi
import com.braczkow.platform.network.api.BluetoothApi
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