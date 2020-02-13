package com.braczkow.platform.di

import android.content.Context
import com.braczkow.platform.location.api.GeocoderApi
import com.braczkow.platform.location.api.GeofenceApi
import com.braczkow.platform.location.api.LocationApi
import com.braczkow.platform.location.internal.*
import com.braczkow.platform.network.api.BluetoothApi
import com.braczkow.platform.network.internal.BluetoothApiImpl
import dagger.Module
import dagger.Provides


@Module
class PlatformModule(
    val context: Context
) {
    @Provides
    fun provideContext() = context

    @Provides
    @PlatformScope
    fun provideLocationApi(impl: LocationApiImpl) : LocationApi = impl

    @Provides
    @PlatformScope
    fun provideGeocoderApi(impl: GeocoderApiImpl): GeocoderApi = impl

    @Provides
    @PlatformScope
    fun provideGeofenceApi(impl: GeofenceApiImpl): GeofenceApi = impl

    @Provides
    @PlatformScope
    fun someUtils(impl: SomeUtilsImpl): SomeUtils = impl

    @Provides
    @PlatformScope
    fun bluetoothApi(impl: BluetoothApiImpl): BluetoothApi = impl
}