package com.android.dropdownmenu.dropdown

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes


fun Context.getLayoutInflater(): LayoutInflater {
    return this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}

fun ViewGroup.inflate(@LayoutRes id: Int): View {
    return LayoutInflater.from(this.context).inflate(id, this, false)
}

fun Int.toDp() = this * Resources.getSystem().displayMetrics.density

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}