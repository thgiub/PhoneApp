package ru.kamaz.itis.bluetooth.presrntation.interfaces

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LifecycleObserver

interface LeftBluetoothSettingsInterface {
    interface View {
        fun init()
        fun setListeners()
        fun changeBluetoothCondition()
        fun whatPairedWhenOpenApp()
        fun allDeviceBt(name: String, mac: String)
    }

    interface Presenter : LifecycleObserver {
        fun setView(view: View)
        fun init()
        fun onBluetoothOnOffBtnClicked()
        fun allDeviceBt(it: BluetoothDevice)
    }
}