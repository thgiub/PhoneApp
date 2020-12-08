package ru.kamaz.itis.bluetooth.presrntation.presenters

import ru.kamaz.itis.bluetooth.presrnters.interfaces.LeftBluetoothSettingsInterface

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
}