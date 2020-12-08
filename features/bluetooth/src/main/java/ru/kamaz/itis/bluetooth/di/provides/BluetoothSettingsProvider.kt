package ru.kamaz.itis.bluetooth.di.provides

import ru.kamaz.itis.bluetooth.di.components.BluetoothSettingsComponent

interface BluetoothSettingsProvider {
    fun bluetoothSettingsComponent(): BluetoothSettingsComponent
}