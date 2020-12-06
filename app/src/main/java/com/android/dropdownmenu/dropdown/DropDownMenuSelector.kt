package com.android.dropdownmenu.dropdown

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dropdownmenu.R
import kotlinx.android.synthetic.main.drop_down_menu_layout.view.*

class DropDownMenuSelector(
    private val context: Context,
    private val builder: DropDownBuilder
) {
    private val dropDownMenuWindow: PopupWindow by lazy {

        // 如果你所在的App做了隐藏状态任务栏，这里需要设置为 isFocusable = false，防止 PopupWindow 获取焦点是手机的状态任务栏显示出来；
        PopupWindow(context).apply {
            isOutsideTouchable = true
            isTouchable = true
            isFocusable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }
    }

    var onDismiss: (() -> Unit)? = null

    private lateinit var contentLayout: View

    init {
        initLayout()
    }

    private fun initLayout() {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentLayout = inflater.inflate(R.layout.drop_down_menu_layout, null)

        contentLayout.recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        contentLayout.recycler_view.adapter = builder.recyclerAdapter

        if (builder.hintText.isNullOrEmpty()) {
            contentLayout.hint_title.hide()
        } else {
            contentLayout.hint_title.text = builder.hintText
            contentLayout.hint_title.show()
        }
        with(dropDownMenuWindow) {
            contentView = contentLayout

            width = if (builder.optionsWidth > 0) {
                setDropDownWidth(builder.optionsWidth)
            } else {
                WindowManager.LayoutParams.WRAP_CONTENT
            }
            height =
                if (builder.visibleOptions > 0 && contentLayout.recycler_view.layoutManager?.itemCount ?: 0 > builder.visibleOptions) {
                    setDropDownHeight(builder.visibleOptions * builder.itemHeight)
                } else {
                    WindowManager.LayoutParams.WRAP_CONTENT
                }

            setOnDismissListener {
                onDismiss?.invoke()
            }
        }
    }

    fun setDropDownWidth(width: Int): Int {
        return width + 50.toDp().toInt()
    }

    fun setDropDownHeight(height: Int): Int {
        return height + 70.toDp().toInt()
    }

    fun show(
        anchorView: View,
        selectPosition: Int = -1,
        mode: DropDownMode = DropDownMode.DROPDOWN_MODE_DEFAULT,
        innerOffsetX: Int = 0,
        innerOffsetY: Int = 0
    ) {
        if (selectPosition != -1) {
            smoothMoveToPosition(selectPosition)
        }

        val location = getLocationOnScreen(anchorView, context)
        val anchorRect = Rect(
            location.x, location.y,
            location.x + anchorView.width,
            location.y + anchorView.height
        )

        val positionX: Int
        val positionY: Int
        when (mode) {
            DropDownMode.DROPDOWN_MODE_CENTER -> {
                positionX = anchorRect.left - dropDownMenuWindow.width / 2
                positionY = anchorRect.top - dropDownMenuWindow.height / 2
            }
            DropDownMode.DROPDOWN_MODE_DEFAULT -> {
                positionX = anchorRect.left - innerOffsetX // innerOffsetX = Shadow + margin
                positionY = anchorRect.top - innerOffsetY
            }
        }
        dropDownMenuWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, positionX, positionY)
    }

    fun dismiss() {
        if (isShowing()) {
            dropDownMenuWindow.dismiss()
        }
    }

    fun isShowing(): Boolean = dropDownMenuWindow.isShowing

    private fun smoothMoveToPosition(position: Int) {
        builder.recyclerAdapter?.setSelected(position)
        (contentLayout.recycler_view.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
            position,
            50.toDp().toInt()
        )
    }

    class DropDownBuilder(private val context: Context) {
        var optionsWidth: Int = 0
        var visibleOptions: Int = 0
        var itemHeight: Int = 60.toDp().toInt()
        var hintText: String? = null
        var recyclerAdapter: DropDownBaseAdapter? = null

        fun setVisibleOptions(value: Int): DropDownBuilder =
            apply { this.visibleOptions = value }

        fun setOptionsWidth(value: Int): DropDownBuilder =
            apply { this.optionsWidth = value }

        fun setItemHeight(value: Int): DropDownBuilder =
            apply { this.itemHeight = value }

        fun setHintText(value: String?): DropDownBuilder =
            apply { this.hintText = value }

        fun setRecyclerAdapter(value: DropDownBaseAdapter): DropDownBuilder =
            apply { this.recyclerAdapter = value }

        fun build(): DropDownMenuSelector = DropDownMenuSelector(
            context = context,
            builder = this@DropDownBuilder
        )
    }
}

enum class DropDownMode(val value: Int) {
    DROPDOWN_MODE_DEFAULT(0),
    DROPDOWN_MODE_CENTER(1),
}

fun getLocationOnScreen(v: View, context: Context): Point {
    val position = IntArray(2)
    v.getLocationOnScreen(position)
    return Point(position[0], position[1] - getStatusBarHeight(context))
}

private fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}