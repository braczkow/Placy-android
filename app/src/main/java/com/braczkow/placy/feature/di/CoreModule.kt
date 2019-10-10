package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.PermissionApi
import com.braczkow.placy.feature.PermissionApiImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {
    @Provides
    @Singleton
    fun providePermissionApi(impl: PermissionApiImpl): PermissionApi = impl
}