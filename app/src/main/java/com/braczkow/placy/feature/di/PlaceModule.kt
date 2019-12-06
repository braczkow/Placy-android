package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.location.GeofenceApi
import com.braczkow.placy.feature.location.GeofenceApiImpl
import com.braczkow.placy.feature.place.*
import com.braczkow.placy.feature.place.impl.GeofencePlaceStorage
import com.braczkow.placy.feature.place.impl.GeofencePlaceStorageImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlaceModule {
    @Provides
    fun provideGeofenceApi(impl: GeofenceApiImpl): GeofenceApi = impl

    @Provides
    fun provideGeofencePlaceApi(impl: GeofencePlaceApiImpl): GeofencePlaceApi = impl

    @Provides
    @Singleton
    fun provideGeofencePlaceStorage(impl: GeofencePlaceStorageImpl): GeofencePlaceStorage = impl

    @Provides
    fun providePlaceListApi(impl: PlaceListApiImpl): PlaceListApi = impl
}