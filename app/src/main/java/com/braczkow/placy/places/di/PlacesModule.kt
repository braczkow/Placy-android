package com.braczkow.placy.places.di

import com.braczkow.placy.places.LocationApi
import com.braczkow.placy.places.LocationApiImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

class SomeApi {
    fun haveFun() = "blabla"
}

@Module
class PlacesModule {
    @Provides
    fun provideSomApi() = SomeApi()

    @Provides
    @Singleton
    fun provideLocationApi(impl: LocationApiImpl) : LocationApi = impl
}