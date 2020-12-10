package com.android.dropdownmenu.dropdown

import android.view.View
import android.view.ViewGroup
import com.android.dropdownmenu.R
import kotlinx.android.synthetic.main.drop_down_menu_item.view.*

class DropDownMenuAdapter<T>(
    val list: MutableList<T>
) : DropDownBaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        return AdminViewHolder(parent.inflate(R.layout.drop_down_menu_item))
    }

    override fun onBindViewHolder(holder: DropDownBaseViewHolder, position: Int) {
        holder.bind(position, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AdminViewHolder(view: View) : DropDownBaseViewHolder(view) {
        override fun <T> bind(position: Int, item: T) {
            with(itemView) {
                if (selectPosition == position) {
                    ic_check?.show()
                } else {
                    ic_check?.invisible()
                }

                if (position == (itemCount - 1)) {
                    left_line.invisible()
                } else {
                    left_line.show()
                }

                // 因为本 Demo 中传入数组是 MutableList<String> 类型，如果传入的是MutableList<对象>，这里需要根据类型来显示相应的数据
                // title.text = item.toString()

                // 这里是为了更好的自定义显示，用回调去适配不同 T （对象）
                onItemDisplay?.invoke(title, position) // 显示下拉菜单选项的文本

                select_item.setOnClickListener {
                    displayText = title.text.toString()
                    onItemSelected?.invoke(position)
                }
            }
        }
    }
}