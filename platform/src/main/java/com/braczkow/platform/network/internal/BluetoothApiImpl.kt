package com.braczkow.platform.network.internal

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.braczkow.platform.network.api.BluetoothApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import javax.inject.Inject

class BluetoothApiImpl @Inject constructor(
    private val context: Context
) : BluetoothApi {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    @UseExperimental(ExperimentalCoroutinesApi::class)
    val channel = ConflatedBroadcastChannel<BluetoothApi.State>()

    @UseExperimental(FlowPreview::class)
    override fun networkState(): Flow<BluetoothApi.State> {
        return channel.asFlow()
    }


    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                readBluetoothState()
            }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun readBluetoothState() {
        val isEnabled = bluetoothAdapter.isEnabled
        Timber.d("Bluetooth state changed: $isEnabled")
        channel.offer(if (isEnabled) BluetoothApi.State.Connected else BluetoothApi.State.Disconnected)
    }

    override fun start() {
        context.registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        readBluetoothState()
    }

}