package ru.kamaz.itis.phoneapp.ui.adapters


import android.R.attr.data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kamaz.itis.phoneapp.R
import ru.kamaz.itis.phoneapp.ui.BtListContract
import ru.kamaz.itis.phoneapp.ui.pojo.AllDevice
import ru.kamaz.itis.phoneapp.ui.pojo.ConnectionDevice
import kotlin.IllegalArgumentException


class BluetoothPairedDevicesAdapter(private val listBT:BtListContract.BtListener) :
    RecyclerView.Adapter<BluetoothPairedDevicesAdapter.VhBase<*>>() {
    private val data: MutableList<Comparable<*>>


    companion object {
        var CONNECTION_DEVICE = 0
        var ALL_DEVICE = 1
        var NAME_DEVICE = 2

    }

    init {
        data=ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhBase<*> {
        val context = parent.context
        return when (viewType) {
            CONNECTION_DEVICE -> {
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.bt_paired_devices_item, parent, false)
                VhConnectionDevice(itemView)
            }
            ALL_DEVICE -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.bt_paired_devices_item, parent, false)
                VhAllDevice(itemView)
            }
            NAME_DEVICE -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.connect_noconnect_item, parent, false)
                VhStatusDeviceBtConnection(itemView)
            }
            else -> throw IllegalAccessException("GG")
        }
    }

    override fun onBindViewHolder(holder: VhBase<*>, position: Int) {
        val element = data[position]

        when (holder) {
            is VhAllDevice -> holder.bind(element as AllDevice)
            is VhConnectionDevice->holder.bind(element as ConnectionDevice)
            is VhStatusDeviceBtConnection->holder.bind(element as String)
            else -> throw IllegalArgumentException("gg")
        }

    }

    override fun getItemViewType(position: Int): Int {
        val comparable= data[position]
        return when (comparable){
            is String->NAME_DEVICE

            else -> throw IllegalArgumentException("Ananasy lublu"+ position)
        }
    }

    override fun getItemCount() = data.size

    abstract class VhBase<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class VhConnectionDevice(itemView: View) : VhBase<ConnectionDevice>(itemView) {
        val btDeviceNameView: TextView? = itemView?.findViewById(R.id.tv_device_name)
        val btMacAddressView: TextView? = itemView?.findViewById(R.id.tv_mac_address)
        override fun bind(item: ConnectionDevice) {
            TODO("Not yet implemented")
        }
    }

    class VhStatusDeviceBtConnection(itemView: View) : VhBase<String>(itemView) {
        val thisDeviceBtStatus: TextView? = itemView?.findViewById(R.id.tv_status_device)
        override fun bind(item: String) {
            TODO("Not yet implemented")
        }
    }

    class VhAllDevice(itemView: View) : VhBase<AllDevice>(itemView) {
        val btDeviceNameView: TextView? = itemView?.findViewById(R.id.tv_device_name)
        val btMacAddressView: TextView? = itemView?.findViewById(R.id.tv_mac_address)
        override fun bind(item: AllDevice) {
            TODO("Not yet implemented")
        }
    }

}


