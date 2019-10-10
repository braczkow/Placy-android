package com.braczkow.placy.base

import com.braczkow.placy.feature.di.CoreModule
import com.braczkow.placy.feature.di.PlacesModule
import com.braczkow.placy.ui.PlaceCreateActivityDaggerComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivitiesSubcomponents::class,
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

    fun placeComponentBuilder() : PlaceCreateActivityDaggerComponent.Builder

}