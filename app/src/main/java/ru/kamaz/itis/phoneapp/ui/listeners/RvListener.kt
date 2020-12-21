package ru.kamaz.itis.phoneapp.ui.listeners

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

class RvListener(context: Context,recyclerView: RecyclerView,private val mLIistener: AdapterView.OnItemClickListener):RecyclerView.OnItemTouchListener {
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)



        fun onItemLongClick(view: View?, position: Int)
    }
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }
}