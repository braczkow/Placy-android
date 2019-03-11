package com.braczkow.placy.core.di

import com.braczkow.placy.core.PermissionApi
import com.braczkow.placy.core.PermissionApiImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {
    @Provides
    @Singleton
    fun providePermissionApi(impl: PermissionApiImpl): PermissionApi = impl
}