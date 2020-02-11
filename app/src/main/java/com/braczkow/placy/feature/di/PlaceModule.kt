package com.braczkow.placy.feature.di

import com.braczkow.placy.app.AppScope
import com.braczkow.placy.feature.place.*
import com.braczkow.placy.feature.place.impl.GeofencePlaceStorage
import com.braczkow.placy.feature.place.impl.GeofencePlaceStorageImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

@Module
class PlaceModule {
    @UseExperimental(InternalCoroutinesApi::class)
    @Provides
    fun provideGeofencePlaceApi(impl: GeofencePlaceApiImpl): GeofencePlaceApi = impl

    @Provides
    @AppScope
    fun provideGeofencePlaceStorage(impl: GeofencePlaceStorageImpl): GeofencePlaceStorage = impl

    @Provides
    fun providePlaceListApi(impl: PlaceListApiImpl): PlaceListApi = impl
}