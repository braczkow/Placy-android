package com.braczkow.placy.feature.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class CancelOnStop(lifecycle: Lifecycle, subscription: Subscription) {
    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                subscription.cancel()
            }
        })
    }
}