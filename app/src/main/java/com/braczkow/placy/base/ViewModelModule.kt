package com.braczkow.placy.base

import androidx.lifecycle.ViewModel
import com.braczkow.placy.ui.home.HomeFragment
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragment.ViewModel::class)
    internal abstract fun bindHomeFragmentViewModel(vm: HomeFragment.ViewModel): ViewModel
}
