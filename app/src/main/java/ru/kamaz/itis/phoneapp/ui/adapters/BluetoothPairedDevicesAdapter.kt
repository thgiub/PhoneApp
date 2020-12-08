package ru.kamaz.itis.phoneapp.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kamaz.itis.phoneapp.R
import ru.kamaz.itis.phoneapp.ui.pojo.BtItem
import ru.kamaz.itis.phoneapp.ui.pojo.RecyclerViewItem
import ru.kamaz.itis.phoneapp.ui.pojo.Title
import kotlin.IllegalArgumentException


class BluetoothPairedDevicesAdapter(private val items: List<RecyclerViewItem>) :
    RecyclerView.Adapter<BluetoothPairedDevicesAdapter.VhBase<*>>() {

    companion object {
        var CONNECTION_DEVICE = 0
        var ALL_DEVICE = 1
        var TITLE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhBase<*> {
        val context = parent.context
        return when (viewType) {
            CONNECTION_DEVICE, ALL_DEVICE -> {
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.bt_paired_devices_item, parent, false)
                VhBluetoothItem(itemView)
            }
            TITLE -> {
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.connect_noconnect_item, parent, false)
                VhTitle(itemView)
            }
            else -> throw IllegalAccessException("GG")
        }
    }

    override fun onBindViewHolder(holder: VhBase<*>, position: Int) {
        val element = items[position]

        when (holder) {
            is VhBluetoothItem -> holder.bind(element as BtItem)
            is VhTitle->holder.bind(element as Title)
            //is VhStatusDeviceBtConnection->holder.bind(element as String)
            else -> throw IllegalArgumentException("gg")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val comparable = items[position]) {
            is Title -> TITLE
            is BtItem -> if (comparable.isConnected) CONNECTION_DEVICE else ALL_DEVICE
            else -> throw IllegalArgumentException("Ananasy lublu"+ position)
        }
    }

    override fun getItemCount() = items.size

    abstract class VhBase<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class VhBluetoothItem(itemView: View) : VhBase<BtItem>(itemView) {
        private val btDeviceNameView: TextView? = itemView.findViewById(R.id.tv_device_name)
        private val btMacAddressView: TextView? = itemView.findViewById(R.id.tv_mac_address)

        override fun bind(item: BtItem) {
            btDeviceNameView?.text = item.btName
            btMacAddressView?.text = item.btMacAddress
        }
    }

    class VhTitle(itemView: View) : VhBase<Title>(itemView) {
        val thisDeviceBtStatus: TextView? = itemView?.findViewById(R.id.tv_status_device)

        override fun bind(item: Title) {
            thisDeviceBtStatus?.text = item.text
        }
    }

}


