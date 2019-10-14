package com.braczkow.placy.base

import com.braczkow.placy.ui.place.CreatePlaceFragment
import dagger.Module

@Module(subcomponents = [
    CreatePlaceFragment.DaggerComponent::class
])
class ActivitiesSubcomponents {
}