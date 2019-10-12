package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.location.GeocoderApi
import com.braczkow.placy.feature.location.GeocoderApiImpl
import com.braczkow.placy.feature.location.LocationApi
import com.braczkow.placy.feature.location.LocationApiImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocationModule {
    @Provides
    @Singleton
    fun provideLocationApi(impl: LocationApiImpl) : LocationApi = impl

    @Provides
    fun provideGeocoderApi(impl: GeocoderApiImpl): GeocoderApi = impl
}