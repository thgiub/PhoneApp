package ru.kamaz.itis.phoneapp.ui

import ru.kamaz.itis.phoneapp.ui.pojo.AllDevice
import ru.kamaz.itis.phoneapp.ui.pojo.ConnectionDevice
import ru.kamaz.itis.phoneapp.ui.pojo.ConnectionName

class BtListContract {

    interface BtListener {
        fun onAllDevice(trailer: AllDevice?, position: Int)
        fun onConnectionDevice(review: ConnectionDevice?, positio: Int)
        fun onName(connectionName:ConnectionName)
    }
}