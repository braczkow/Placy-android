package com.braczkow.placy.base

import com.braczkow.placy.feature.di.UtilModule
import com.braczkow.placy.feature.di.LocationModule
import com.braczkow.placy.feature.di.PlaceModule
import com.braczkow.placy.ui.place.create.CreatePlaceFragment
import com.braczkow.placy.ui.place.set_name.SetNameFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        Subcomponents::class,
        UtilModule::class,
        LocationModule::class,
        PlaceModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }

    fun placeComponentBuilder() : CreatePlaceFragment.DaggerComponent.Builder
    fun setNameBuilder() : SetNameFragment.DaggerComponent.Builder

}