package com.android.dropdownmenu.dropdown

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class DropDownBaseAdapter : RecyclerView.Adapter<DropDownBaseViewHolder>() {

    var selectPosition: Int = -1
    var displayText = ""
    var onItemDisplay: ((text : TextView ,position: Int) -> Unit)? = null
    var onItemSelected: ((position: Int) -> Unit)? = null

    fun setSelected(position: Int) {
        this.selectPosition = position
        notifyDataSetChanged()
    }


}