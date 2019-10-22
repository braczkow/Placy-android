package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.place.PlaceApi
import com.braczkow.placy.feature.place.PlaceApiImpl
import dagger.Module
import dagger.Provides

@Module
class PlaceModule {
    @Provides
    fun providePlaceApi(impl: PlaceApiImpl): PlaceApi = impl
}