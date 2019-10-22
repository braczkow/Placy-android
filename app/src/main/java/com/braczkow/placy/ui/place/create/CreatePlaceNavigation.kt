package com.braczkow.placy.ui.place.create

import com.google.android.gms.maps.model.LatLng

interface CreatePlaceNavigation {
    sealed class Event {
        data class ProceedWith(val latLng: LatLng) : Event()
    }

    fun navigate(event: Event)
}

