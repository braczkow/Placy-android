package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.LocationApi
import com.braczkow.placy.feature.LocationApiImpl
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