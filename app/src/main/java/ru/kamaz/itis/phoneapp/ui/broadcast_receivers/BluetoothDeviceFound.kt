package ru.kamaz.itis.phoneapp.ui.broadcast_receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class BluetoothDeviceFound : BroadcastReceiver() {
    private var onDeviceFoundListener: ((BluetoothDevice) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                onDeviceFoundListener?.invoke(device)
                Log.d("Bluetooth", "New device found!")
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> { Log.d("Bluetooth", "Discovering started!") }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> { Log.d("Bluetooth", "Discovering finished!") }
        }
    }


    fun setOnDeviceFoundListener(listener: (BluetoothDevice) -> Unit) {
        onDeviceFoundListener = listener
    }

}
