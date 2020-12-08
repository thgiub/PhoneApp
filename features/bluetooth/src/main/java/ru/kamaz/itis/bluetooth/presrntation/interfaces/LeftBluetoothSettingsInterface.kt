package ru.kamaz.itis.bluetooth.presrnters.interfaces

import androidx.lifecycle.LifecycleObserver

interface LeftBluetoothSettingsInterface {
    interface View {
        fun init()
        fun setListeners()
        fun changeBluetoothCondition()
        fun whatPairedWhenOpenApp()



    }

    interface Presenter : LifecycleObserver {
        fun setView(view: View)
        fun init()
        fun onBluetoothOnOffBtnClicked()
    }
}