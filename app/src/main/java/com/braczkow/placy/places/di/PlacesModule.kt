package com.braczkow.placy.places.di

import dagger.Module
import dagger.Provides

class SomeApi {
    fun haveFun() = "blabla"
}

@Module
class PlacesModule {
    @Provides
    fun provideSomApi() = SomeApi()
}