package ru.kamaz.itis.phoneapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Test : AppCompatActivity() {
    companion object {
        const val REQUEST_ENABLE_BT = 42
//        val REQUEST_QUERY_DEVICES = 142
    }

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(applicationContext, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        } else if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        val permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
        val permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
        val permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val permission4 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission1 != PackageManager.PERMISSION_GRANTED
            || permission2 != PackageManager.PERMISSION_GRANTED
            || permission3 != PackageManager.PERMISSION_GRANTED
            || permission4 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                642)
        } else {
            Log.d("DISCOVERING-PERMISSIONS", "Permissions Granted")
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if (bluetoothAdapter?.isDiscovering == true) {
                bluetoothAdapter?.cancelDiscovery()
            }
            var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            this.registerReceiver(receiver, filter)
            filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            this.registerReceiver(receiver, filter)
            filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            this.registerReceiver(receiver, filter)
            bluetoothAdapter?.startDiscovery()
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device!!.name
                    val deviceHardwareAddress = device!!.address // MAC address
                    var msg = ""
                    if (deviceName.isNullOrBlank())
                    {
                        msg = deviceHardwareAddress
                    } else {
                        msg = "$deviceName $deviceHardwareAddress"
                    }
                    Log.d("DISCOVERING-DEVICE", msg)
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("DISCOVERING-STARTED", "isDiscovering")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("DISCOVERING-FINISHED", "FinishedDiscovering")
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}