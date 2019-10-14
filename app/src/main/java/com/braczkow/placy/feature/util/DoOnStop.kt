package com.braczkow.placy.feature.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class DoOnStop(lifecycle: Lifecycle, action: () -> Unit) {
    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                action()
            }
        })
    }
}