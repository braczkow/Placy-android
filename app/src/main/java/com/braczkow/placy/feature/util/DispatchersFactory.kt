package com.braczkow.placy.feature.util

import kotlinx.coroutines.Dispatchers

class DispatchersFactory {
    fun main() = Dispatchers.Main
    fun io() = Dispatchers.IO
}