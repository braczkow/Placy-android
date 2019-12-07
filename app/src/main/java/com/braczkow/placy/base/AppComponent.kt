package com.braczkow.placy.base

import com.braczkow.placy.feature.di.UtilModule
import com.braczkow.placy.feature.di.LocationModule
import com.braczkow.placy.feature.di.PlaceModule
import com.braczkow.placy.feature.di.StorageModule
import com.braczkow.placy.feature.location.GeofenceApi
import com.braczkow.placy.ui.home.HomeFragment
import com.braczkow.placy.ui.place.create.CreatePlaceFragment
import com.braczkow.placy.ui.place.set_name.SetNameFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        Subcomponents::class,
        ViewModelModule::class,
        UtilModule::class,
        LocationModule::class,
        PlaceModule::class,
        StorageModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }

    fun placeComponentBuilder(): CreatePlaceFragment.DaggerComponent.Builder
    fun setNameBuilder(): SetNameFragment.DaggerComponent.Builder

    fun inject(homeFragment: HomeFragment)

    fun geofenceApi(): GeofenceApi
}