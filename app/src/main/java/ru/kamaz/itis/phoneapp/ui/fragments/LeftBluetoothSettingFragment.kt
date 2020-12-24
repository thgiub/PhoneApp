package ru.kamaz.itis.phoneapp.ui.fragments


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.session.MediaSession
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.setting_left_fragment.*
import ru.kamaz.itis.bluetooth.di.provides.BluetoothSettingsProvider
import ru.kamaz.itis.bluetooth.presrntation.interfaces.LeftBluetoothSettingsInterface
import ru.kamaz.itis.phoneapp.R
import ru.kamaz.itis.phoneapp.ui.App
import ru.kamaz.itis.phoneapp.ui.adapters.BluetoothPairedDevicesAdapter
import ru.kamaz.itis.phoneapp.ui.broadcast_receivers.BluetoothDeviceFound
import ru.kamaz.itis.phoneapp.ui.listeners.RvListener
import ru.kamaz.itis.phoneapp.ui.pojo.BtItem
import ru.kamaz.itis.phoneapp.ui.pojo.RecyclerViewItem
import ru.kamaz.itis.phoneapp.ui.pojo.Title
import java.util.*


class LeftBluetoothSettingFragment : Fragment(), LeftBluetoothSettingsInterface.View {
    companion object {
        const val REQUEST_ENABLE_BT = 42
    }
    lateinit var presenter: LeftBluetoothSettingsInterface.Presenter
    private lateinit var adapter: BluetoothAdapter
    private val receiver = BluetoothDeviceFound()


    override fun init() {
        checkDevicePermission()

        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter?.let {
            this.adapter = it
            if (!adapter.isEnabled) startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)
        } ?: adapterNotSupported()

        whatPairedWhenOpenApp()

        if (adapter.isDiscovering) {
            adapter.cancelDiscovery()
        }
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context?.registerReceiver(receiver, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        context?.registerReceiver(receiver, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context?.registerReceiver(receiver, filter)
        adapter.startDiscovery()
        val listList = mutableListOf<RecyclerViewItem>(Title("Сопряженные устройства"))
        listList.addAll(queryPairedDevices())
        listList.add(Title("Все устиройства"))

        rv_btList.adapter = BluetoothPairedDevicesAdapter(listList)
        rv_btList.layoutManager= LinearLayoutManager(context)
    }

    private fun checkDevicePermission() {
        val permission1 = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.BLUETOOTH) }
        val permission2 = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.BLUETOOTH_ADMIN) }
        val permission3 = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) }
        val permission4 = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permission1 != PackageManager.PERMISSION_GRANTED
            || permission2 != PackageManager.PERMISSION_GRANTED
            || permission3 != PackageManager.PERMISSION_GRANTED
            || permission4 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                642)
        } else {
            Log.d("DISCOVERING-PERMISSIONS", "Permissions Granted")
        }
    }

    override fun setListeners() {

        receiver.setOnDeviceFoundListener {
            (rv_btList.adapter as BluetoothPairedDevicesAdapter).addNewDevice(BtItem(it.name ?: "", it.address ?: ""))


        }
    }
    fun connect(btBevice:BluetoothDevice){
        val uuid: UUID = btBevice?.uuids?.get(0)!!.uuid
        val blue = btBevice.createRfcommSocketToServiceRecord(uuid)
        blue.connect()
    }

    private fun adapterNotSupported() {
        Log.e("have bluetooth", "device not support Bluetooth")
        Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        //TODO() Выйти из приложения
    }

    private fun queryPairedDevices(): MutableList<RecyclerViewItem> {
        val pairedDevices: Set<BluetoothDevice> = adapter.bondedDevices
        val list = mutableListOf<RecyclerViewItem>()

        pairedDevices.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
            val item = BtItem(deviceName,deviceHardwareAddress)
            list += item
        }

        return list

    }

    override fun changeBluetoothCondition() {
        if (!adapter.isEnabled) {
            adapter.enable()
            val toast = Toast.makeText(context, "Bluetooth on", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            adapter.disable()
            val toast = Toast.makeText(context, "Bluetooth off", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun whatPairedWhenOpenApp() {

    }

    override fun allDeviceBt(name: String, mac: String) {
        (rv_btList.adapter as BluetoothPairedDevicesAdapter).addNewDevice(BtItem(name, mac))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_left_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = (App.appContext as BluetoothSettingsProvider).bluetoothSettingsComponent()
            .getBluetoothSettingsPresenter()
        lifecycle.addObserver(presenter)
        presenter.setView(this)
        presenter.init()
    }

    override fun onDestroy() {
        context?.unregisterReceiver(receiver)
        super.onDestroy()
    }
}






