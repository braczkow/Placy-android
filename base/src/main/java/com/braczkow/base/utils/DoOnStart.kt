package com.braczkow.base.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class DoOnStart(lifecycle: Lifecycle, action: () -> Unit) {
    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                action()
            }
        })
    }
}