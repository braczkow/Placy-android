package com.braczkow.placy.feature.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableOnStop(lifecycle: Lifecycle) {

    private val disposables = CompositeDisposable()

    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                disposables.dispose()
            }
        })
    }

    fun add(disposable: Disposable) {
        disposables.add(disposable)
    }
}