package ru.kamaz.itis.bluetooth.di.components

import dagger.Subcomponent
import ru.kamaz.itis.bluetooth.di.modules.CacheModule
import ru.kamaz.itis.bluetooth.di.modules.DataModule
import ru.kamaz.itis.bluetooth.di.modules.DomainModule
import ru.kamaz.itis.bluetooth.di.modules.PresentationModule
import ru.kamaz.itis.bluetooth.presrnters.interfaces.LeftBluetoothSettingsInterface
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [PresentationModule::class, DomainModule::class, DataModule::class, CacheModule::class])
interface BluetoothSettingsComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create():BluetoothSettingsComponent
    }

    fun getBluetoothSettingsPresenter(): LeftBluetoothSettingsInterface.Presenter
}