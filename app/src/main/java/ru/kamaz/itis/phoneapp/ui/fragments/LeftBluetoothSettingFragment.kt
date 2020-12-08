package ru.kamaz.itis.phoneapp.ui.fragments
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.setting_left_fragment.*
import ru.kamaz.itis.bluetooth.di.provides.BluetoothSettingsProvider
import ru.kamaz.itis.bluetooth.presrnters.interfaces.LeftBluetoothSettingsInterface
import ru.kamaz.itis.phoneapp.R
import ru.kamaz.itis.phoneapp.ui.App
import ru.kamaz.itis.phoneapp.ui.adapters.BluetoothPairedDevicesAdapter


class LeftBluetoothSettingFragment : Fragment(), LeftBluetoothSettingsInterface.View {
    lateinit var presenter: LeftBluetoothSettingsInterface.Presenter
    private lateinit var adapter: BluetoothAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        presenter = (App.appContext as BluetoothSettingsProvider).bluetoothSettingsComponent()
            .getBluetoothSettingsPresenter()
        lifecycle.addObserver(presenter)

        return inflater.inflate(R.layout.setting_left_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.init()

    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action!!
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {

                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    val item = BtItem(deviceName,deviceHardwareAddress)


                }
            }
        }
    }

    override fun init() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter?.let { this.adapter = it } ?: adapterNotSupported()
        whatPairedWhenOpenApp()

        val listList = queryPairedDevices()

        rv_btList.adapter=BluetoothPairedDevicesAdapter(listList)
        rv_btList.layoutManager= LinearLayoutManager(context)
    }

    private fun adapterNotSupported() {
        Log.e("have bluetooth", "device not support Bluetooth")
        //TODO() Выйти из приложения
    }

    override fun setListeners() {
        switchOnOffBt.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                presenter.onBluetoothOnOffBtnClicked()

            } else {
                presenter.onBluetoothOnOffBtnClicked()

            }
        }
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


     fun queryPairedDevices(): List<BtItem> {

         val pairedDevices: Set<BluetoothDevice> = adapter.getBondedDevices()
         val list = ArrayList<BtItem>()

         pairedDevices?.forEach { device ->
             val deviceName = device.name
             val deviceHardwareAddress = device.address
            val item = BtItem(deviceName,deviceHardwareAddress)
             list+=item
         }
         return list

     }
    override fun whatPairedWhenOpenApp() {
        if (!adapter.isEnabled) {
            switchOnOffBt.setChecked(false)
        } else {
            switchOnOffBt.setChecked(true)
        }
    }
}





