package ru.kamaz.itis.bluetooth.presrntation.presenters

import android.bluetooth.BluetoothDevice
import ru.kamaz.itis.bluetooth.presrntation.interfaces.LeftBluetoothSettingsInterface

internal class LeftBluetoothSettingsPresenter(): LeftBluetoothSettingsInterface.Presenter {
    private lateinit var view: LeftBluetoothSettingsInterface.View

    override fun setView(view: LeftBluetoothSettingsInterface.View) {
       this.view=view
    }

    override fun init() {
        view.init()
        view.setListeners()
    }

    override fun onBluetoothOnOffBtnClicked() {
        view.changeBluetoothCondition()
    }

    override fun allDeviceBt(it: BluetoothDevice) {
        view.allDeviceBt(it.name, it.address)
    }
}