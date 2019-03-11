package com.braczkow.placy.base

import com.braczkow.placy.core.di.CoreModule
import com.braczkow.placy.places.di.PlacesComponent
import com.braczkow.placy.places.di.PlacesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubcomponentsModule::class,
        CoreModule::class,
        PlacesModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }

    fun placeComponentBuilder() : PlacesComponent.Builder

}