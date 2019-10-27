package com.braczkow.placy.feature.storage

interface Storage {
    fun <T> load(type: Class<T>): T?
    fun <T : Any> save(item: T)
}