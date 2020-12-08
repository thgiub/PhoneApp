package ru.kamaz.itis.phoneapp.ui

import android.app.Application
import android.content.Context
import ru.kamaz.itis.bluetooth.di.components.BluetoothSettingsComponent
import ru.kamaz.itis.bluetooth.di.provides.BluetoothSettingsProvider
import ru.kamaz.itis.phoneapp.di.component.AppComponent
import ru.kamaz.itis.phoneapp.di.component.DaggerAppComponent

class App : Application(), BluetoothSettingsProvider{
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var appContext: Context
        private var bluetoothSettingsComponent: BluetoothSettingsComponent? = null
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.create()
    }

    override fun bluetoothSettingsComponent(): BluetoothSettingsComponent {
       if (bluetoothSettingsComponent == null)
           bluetoothSettingsComponent = appComponent.createLeftBluetoothSettingsComponent().create()
        return bluetoothSettingsComponent!!
    }


}