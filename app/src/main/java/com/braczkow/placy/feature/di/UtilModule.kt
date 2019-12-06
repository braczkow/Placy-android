package com.braczkow.placy.feature.di

import com.braczkow.placy.feature.PermissionApi
import com.braczkow.placy.feature.PermissionApiImpl
import com.braczkow.placy.feature.util.DispatchersFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilModule {
    @Provides
    @Singleton
    fun providePermissionApi(impl: PermissionApiImpl): PermissionApi = impl

    @Provides
    fun provideDispatchersFactory() = DispatchersFactory()
}