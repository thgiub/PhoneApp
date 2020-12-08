package ru.kamaz.itis.phoneapp.ui.pojo

class BtItem (
    val btName: String,
    val btMacAddress: String,
    val isConnected: Boolean = false
) : RecyclerViewItem