package com.braczkow.placy.ui.place.set_name

interface SetNameNavigation {
    sealed class Event {
        object Finish: Event()
    }

    fun navigate(event: Event)
}