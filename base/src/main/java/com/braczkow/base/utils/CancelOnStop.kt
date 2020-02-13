package com.braczkow.base.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class CancelOnStop(lifecycle: Lifecycle, subscription: com.braczkow.base.utils.Subscription) {
    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                subscription.cancel()
            }
        })
    }
}