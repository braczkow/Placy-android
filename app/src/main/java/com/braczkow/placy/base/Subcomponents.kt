package com.braczkow.placy.base

import com.braczkow.placy.ui.place.CreatePlaceFragment
import com.braczkow.placy.ui.place.SetNameFragment
import dagger.Module

@Module(
    subcomponents = [
        CreatePlaceFragment.DaggerComponent::class,
        SetNameFragment.DaggerComponent::class
    ]
)
class Subcomponents {
}