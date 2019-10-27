package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.place.GeofenceApi
import com.braczkow.placy.feature.place.GeofenceApiImpl
import com.braczkow.placy.feature.place.GeofencePlaceApi
import com.braczkow.placy.feature.place.GeofencePlaceApiImpl
import dagger.Module
import dagger.Provides

@Module
class PlaceModule {
    @Provides
    fun provideGeofenceApi(impl: GeofenceApiImpl): GeofenceApi = impl

    @Provides
    fun provideGeofencePlaceApi(impl: GeofencePlaceApiImpl): GeofencePlaceApi = impl
}