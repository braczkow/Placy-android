package com.braczkow.placy.feature.di

import com.braczkow.placy.app.AppScope
import com.braczkow.placy.feature.PermissionApi
import com.braczkow.placy.feature.PermissionApiImpl
import com.braczkow.placy.feature.util.DispatchersFactory
import dagger.Module
import dagger.Provides

@Module
class UtilModule {
    @Provides
    @AppScope
    fun providePermissionApi(impl: PermissionApiImpl): PermissionApi = impl

    @Provides
    fun provideDispatchersFactory() = DispatchersFactory()
}