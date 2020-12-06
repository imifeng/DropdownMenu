package com.android.dropdownmenu.dropdown

import androidx.recyclerview.widget.RecyclerView

abstract class DropDownBaseAdapter : RecyclerView.Adapter<DropDownBaseViewHolder>() {

    var selectPosition: Int = -1
    var displayText = ""
    var onItemSelected: ((position: Int) -> Unit)? = null

    fun setSelected(position: Int) {
        this.selectPosition = position
        notifyDataSetChanged()
    }


}