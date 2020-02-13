package com.braczkow.base.utils

import kotlinx.coroutines.Dispatchers

class DispatchersFactory {
    fun main() = Dispatchers.Main
    fun io() = Dispatchers.IO
}