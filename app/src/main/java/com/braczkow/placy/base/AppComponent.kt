package com.braczkow.placy.base

import com.braczkow.placy.feature.di.UtilModule
import com.braczkow.placy.feature.di.LocationModule
import com.braczkow.placy.ui.PlaceCreateFragmentDaggerComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivitiesSubcomponents::class,
        UtilModule::class,
        LocationModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }

    fun placeComponentBuilder() : PlaceCreateFragmentDaggerComponent.Builder

}