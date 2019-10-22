package com.braczkow.placy.base

import com.braczkow.placy.ui.place.create.CreatePlaceFragment
import com.braczkow.placy.ui.place.set_name.SetNameFragment
import dagger.Module

@Module(
    subcomponents = [
        CreatePlaceFragment.DaggerComponent::class,
        SetNameFragment.DaggerComponent::class
    ]
)
class Subcomponents {
}