package ru.kamaz.itis.phoneapp.di.component

import dagger.Component
import ru.kamaz.itis.bluetooth.di.components.BluetoothSettingsComponent

@Component
interface AppComponent {
    fun createLeftBluetoothSettingsComponent(): BluetoothSettingsComponent.Factory
}