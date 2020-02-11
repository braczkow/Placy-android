package com.braczkow.placy.platform.network.api

import kotlinx.coroutines.flow.Flow

interface BluetoothApi {
    sealed class State {
        object Connected: State()
        object Disconnected: State()
    }

    fun start()
    fun networkState(): Flow<BluetoothApi.State>
}
