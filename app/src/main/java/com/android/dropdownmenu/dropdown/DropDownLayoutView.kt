package com.android.dropdownmenu.dropdown

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.android.dropdownmenu.R
import kotlinx.android.synthetic.main.drop_down_layout.view.*

/**
 * Dropdown menu UI componentization
 *
 * @param context
 * @param attrs
 * @param defStyleAttr
 */
@SuppressLint("ResourceType")
class DropDownLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var selectPosition = -1
    var dropdownMenuSelector: DropDownMenuSelector? = null
    var dropDownAdapter: DropDownBaseAdapter? = null

    private val innerIcon: Int
    private val innerText: String?
    private var innerTextSelectedFont: Int
    private val innerTextFont: Int
    private val innerTextSize: Float
    private val innerTextColor: Int
    private val innerMenuHintTitle: String?
    private var innerMenuWidth: Float
    private var innerVisibleOptions: Int
    private var innerItemHeight: Float
    private val innerOffsetX: Float
    private val innerOffsetY: Float

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DropDownLayout, 0, 0).apply {
            innerIcon = getResourceId(R.styleable.DropDownLayout_innerIcon, 0)
            innerText = getString(R.styleable.DropDownLayout_innerText)
            innerTextSelectedFont = getResourceId(
                R.styleable.DropDownLayout_innerTextSelectedFont,
                0
            )
            innerTextFont = getResourceId(
                R.styleable.DropDownLayout_innerTextFont,
                0
            )
            innerTextSize = getDimension(
                R.styleable.DropDownLayout_innerTextSize,
                0f
            )
            innerTextColor =
                getColor(R.styleable.DropDownLayout_innerTextColor, R.color.primary_black)
            innerMenuHintTitle = getString(R.styleable.DropDownLayout_innerMenuHintTitle)
            innerMenuWidth = getDimension(
                R.styleable.DropDownLayout_innerMenuWidth, 0f
            )
            innerItemHeight = getDimension(
                R.styleable.DropDownLayout_innerItemHeight, 60.toDp()
            )
            innerVisibleOptions = getInt(
                R.styleable.DropDownLayout_innerVisibleOptions, 0
            )
            innerOffsetX = getDimension(
                R.styleable.DropDownLayout_innerOffsetX,
                40.toDp()
            )
            innerOffsetY = getDimension(
                R.styleable.DropDownLayout_innerOffsetY,
                25.toDp()
            )
            if (innerTextSelectedFont == 0) {
                if (innerTextFont != 0) {
                    innerTextSelectedFont = innerTextFont
                }
            }
            recycle()
        }

        context.getLayoutInflater().inflate(R.layout.drop_down_layout, this, true)
        if (innerIcon == 0) {
            drop_down_icon.hide()
        } else {
            drop_down_icon.setImageResource(innerIcon)
            drop_down_icon.show()
        }
        if (!innerText.isNullOrEmpty()) {
            drop_down_text.text = innerText
        }
        if (innerTextFont > 0) {
            drop_down_text.typeface = ResourcesCompat.getFont(context, innerTextFont)
        }
        drop_down_text.setTextColor(innerTextColor)
        if (innerTextSize > 0) {
            drop_down_text.textSize = innerTextSize
        }

        setOnClickListener { show() }

        setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dropdownMenuSelector?.isShowing() == true) {
                        dismiss()
                        return true
                    }
                }
                return false
            }
        })
    }

    fun setMenuWidth(width: Float) {
        this.innerMenuWidth = width
    }

    fun setVisibleOptions(options: Int) {
        this.innerVisibleOptions = options
    }

    fun setSelection(selectedPosition: Int) {
        this.selectPosition = selectedPosition
        if (innerTextFont > 0 && innerTextSelectedFont > 0)
            if (selectPosition == -1) {
                drop_down_text.typeface = ResourcesCompat.getFont(context, innerTextFont)
            } else {
                drop_down_text.typeface = ResourcesCompat.getFont(context, innerTextSelectedFont)
            }
    }

    /**
     *
     * @param T
     * @param list
     * @param preText               The text to be displayed
     * @param autoSelected          Auto Selected
     * @param autoSelectedPosition  Auto Selected Position
     * @param onSelectionDisplay    Display each selection
     * @param onSelected            Callback after selection
     * @param width                 The width of the drop-down menu can be set
     * @param options               Swipe to select the visible number to limit the height of the drop-down menu
     */
    fun <T> setDropDownAdapter(
        list: MutableList<T>,
        preText: String? = null,
        autoSelected: Boolean = false,
        autoSelectedPosition: Int = -1,
        onSelectionDisplay: ((text : TextView, position: Int) -> Unit)? = null,
        onSelected: ((position: Int) -> Unit)? = null,
        width: Float = 0f,
        options: Int = 0
    ) {
        if (autoSelected) {
            if (autoSelectedPosition > -1) {
                setSelection(autoSelectedPosition)
            } else {
                setSelection(0)
            }
        }
        dropDownAdapter = DropDownMenuAdapter(list).apply {
            onItemDisplay = { text: TextView, position: Int ->
                onSelectionDisplay?.invoke(text, position)
            }
            onItemSelected = {
                if (it > -1) {
                    setSelection(it)
                    drop_down_text.text = displayText
                    onSelected?.invoke(it)
                    dismiss()
                }
            }
        }

        if (!preText.isNullOrEmpty()) {
            syncSelectedText(preText)
        }

        if (width > 0) {
            setMenuWidth(width)
        }

        if (options > 0) {
            setVisibleOptions(options)
        }

        dropdownMenuSelector = DropDownMenuSelector.DropDownBuilder(context)
            .setOptionsWidth(innerMenuWidth.toInt())
            .setVisibleOptions(innerVisibleOptions)
            .setItemHeight(innerItemHeight.toInt())
            .setHintText(innerMenuHintTitle)
            .setRecyclerAdapter(dropDownAdapter!!)
            .build()

        dropdownMenuSelector?.onDismiss = {
            dismissFocus()
        }
    }

    // 需要同步更新你选择后的值
    fun syncSelectedText(selected: String) {
        drop_down_text?.text = selected
    }

    private fun show() {
        dropdownMenuSelector?.show(
            this, selectPosition,
            DropDownMode.DROPDOWN_MODE_DEFAULT, innerOffsetX.toInt(), innerOffsetY.toInt()
        ).also {
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    private fun dismiss() {
        dropdownMenuSelector?.dismiss()
        dismissFocus()
    }

    // Avoid taking up focus, release focus
    fun dismissFocus() {
        isFocusableInTouchMode = false
        requestFocus(FOCUS_UP)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dismiss()
    }

    // TODO 配合 PopupWindow 设置 isFocusable = false 使用，防止View丢失焦点后 PopupWindow 仍然显示在前台
//    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
//        super.onWindowFocusChanged(hasWindowFocus)
//        dismiss()
//    }

}