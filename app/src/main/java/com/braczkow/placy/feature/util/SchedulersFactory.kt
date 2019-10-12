package com.braczkow.placy.feature.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SchedulersFactory {
    fun mainThread() = AndroidSchedulers.mainThread()
    fun io() = Schedulers.io()
}