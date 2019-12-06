package com.braczkow.placy.feature.util

interface Observer<T> {
    fun next(item: T)
}

interface Subscription {
    fun cancel()
}

interface Value<T> {
    fun subscribe(observer: (T) -> Unit): Subscription
}

interface Emitter<T> {
    fun emit(item: T)
}

class ValueEmitter<T> : Value<T>, Emitter<T> {
    private val observers = mutableSetOf<(T) -> Unit>()
    private var lastItem: T? = null

    override fun subscribe(observer: (T) -> Unit): Subscription {
        lastItem?.let { observer.invoke(it) }
        observers.add(observer)

        return object : Subscription {
            override fun cancel() {
                observers.remove(observer)
            }
        }
    }

    override fun emit(item: T) {
        lastItem = item

        observers.forEach {
            it(item)
        }
    }
}