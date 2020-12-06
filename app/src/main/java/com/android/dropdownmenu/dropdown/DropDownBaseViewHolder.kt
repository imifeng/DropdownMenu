package com.android.dropdownmenu.dropdown

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DropDownBaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun <T> bind(position: Int, item: T)
}