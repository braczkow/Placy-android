package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.storage.Storage
import com.braczkow.placy.feature.storage.StorageImpl
import dagger.Module
import dagger.Provides

@Module
class StorageModule {
    @Provides
    fun provideStorage(impl: StorageImpl): Storage = impl
}