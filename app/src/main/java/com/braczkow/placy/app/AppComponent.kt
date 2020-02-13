package com.braczkow.placy.app

import com.braczkow.placy.feature.di.*
import com.braczkow.platform.location.api.GeofenceApi
import com.braczkow.platform.di.PlatformComponent
import com.braczkow.placy.ui.home.HomeFragment
import com.braczkow.placy.ui.place.create.CreatePlaceFragment
import com.braczkow.placy.ui.place.set_name.SetNameFragment
import dagger.Component


@AppScope
@Component(
    modules = [
        AppModule::class,
        Subcomponents::class,
        ViewModelModule::class,
        UtilModule::class,
        PlaceModule::class,
        StorageModule::class,
        DbModule::class
    ],
    dependencies = [
        PlatformComponent::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun plus(platformComponent: PlatformComponent): Builder
        fun appModule(appModule: AppModule): Builder
    }

    fun inject(app: App)

    fun placeComponentBuilder(): CreatePlaceFragment.DaggerComponent.Builder
    fun setNameBuilder(): SetNameFragment.DaggerComponent.Builder

    fun inject(homeFragment: HomeFragment)

    fun geofenceApi(): GeofenceApi
}