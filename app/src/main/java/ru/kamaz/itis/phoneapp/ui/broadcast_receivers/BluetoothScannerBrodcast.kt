package ru.kamaz.itis.phoneapp.ui.broadcast_receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log
import java.lang.Exception

class BluetoothScannerBrodcast(val context: Context) :BroadcastReceiver(), BluetoothScanner {
    private var listener: BluetoothScanner.ScanningListener? = null
    private var onDeviceFoundListener: ((BluetoothDevice) -> Unit)? = null


    private var isDiscovering: Boolean = false

    private val handler: Handler = Handler()

    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val foundDevices = HashMap<String, BluetoothDevice>()

    private var isListeningForDiscoverableStatus = false

    private val foundDeviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    private val foundDeviceReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                device?.let { onDeviceFoundListener?.invoke(it) }

            }
        }
    }

    private val discoverableStateFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
    private val discoverableStateReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE)

            if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                listener?.onDiscoverableStart()
            } else {
                listener?.onDiscoverableFinish()
                isListeningForDiscoverableStatus = false
                context.unregisterReceiver(this)
            }
        }
    }

    private val scanningFinishedTask = Runnable {

        listener?.onDiscoveryFinish()
        isDiscovering = false

        try {
            context.unregisterReceiver(foundDeviceReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        if (adapter != null && adapter.isDiscovering) {
            adapter.cancelDiscovery()
        }
    }

    override fun getMyDeviceName() =
        try {
            adapter?.name ?: "?"
        } catch (e: Exception) {
            "?"
        }

    override fun scanForDevices(seconds: Int) {

        adapter?.startDiscovery()
        listener?.onDiscoveryStart(seconds)
        isDiscovering = true

        handler.postDelayed(scanningFinishedTask, seconds.toLong() * 1000)
        context.registerReceiver(foundDeviceReceiver, foundDeviceFilter)
    }

    override fun stopScanning() {
        handler.removeCallbacks(scanningFinishedTask)
        scanningFinishedTask.run()
    }

    override fun getBondedDevices(): List<BluetoothDevice> {
        val devices = adapter?.bondedDevices
        return if (devices == null) ArrayList() else ArrayList<BluetoothDevice>(devices)
    }

    override fun getDeviceByAddress(address: String): BluetoothDevice? {
        val pairedDevice = getBondedDevices()
            .filter { it.address.equals(address, ignoreCase = true) }
        return if (!pairedDevice.isEmpty()) pairedDevice.first() else foundDevices[address]
    }

    override fun isBluetoothAvailable() = adapter != null

    override fun isBluetoothEnabled() = adapter?.isEnabled ?: false

    override fun isDiscoverable() =
        adapter?.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE

    override fun isDiscovering() = isDiscovering

    override fun listenDiscoverableStatus() {
        isListeningForDiscoverableStatus = true
        context.registerReceiver(discoverableStateReceiver, discoverableStateFilter)
    }

    override fun stopListeningDiscoverableStatus() {
        if (isListeningForDiscoverableStatus) {
            context.unregisterReceiver(discoverableStateReceiver)
            isListeningForDiscoverableStatus = false
        }
    }

    override fun setScanningListener(listener: BluetoothScanner.ScanningListener) {
        this.listener = listener
    }

    fun setOnDeviceFoundListener(listener: (BluetoothDevice) -> Unit) {
        onDeviceFoundListener = listener
    }


    override fun onReceive(p0: Context?, intent: Intent?) {
        if (BluetoothDevice.ACTION_FOUND == intent?.action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            device?.let { onDeviceFoundListener?.invoke(it) }
            Log.d("Bluetooth", "New device found!")
        }
    }
}
