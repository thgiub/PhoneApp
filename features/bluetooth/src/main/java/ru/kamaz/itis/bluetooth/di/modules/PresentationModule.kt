package ru.kamaz.itis.bluetooth.di.modules

import dagger.Module
import dagger.Provides
import ru.kamaz.itis.bluetooth.presrntation.presenters.LeftBluetoothSettingsPresenter
import ru.kamaz.itis.bluetooth.presrnters.interfaces.LeftBluetoothSettingsInterface
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    fun provideLeftBluetoothSettingPresenter(): LeftBluetoothSettingsInterface.Presenter {
        return LeftBluetoothSettingsPresenter()
    }
}