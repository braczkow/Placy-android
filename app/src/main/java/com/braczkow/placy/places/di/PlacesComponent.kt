package com.braczkow.placy.places.di

import com.braczkow.placy.places.PlaceCreateActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf())
interface PlacesComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): PlacesComponent
    }

    fun inject(placeCreateActivity: PlaceCreateActivity)
}