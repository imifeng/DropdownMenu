package com.android.dropdownmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val testList =
        mutableListOf("1-AAAAAA", "2-BBBBBB", "3-CCCCCC", "4-DDDDDD", "5-EEEEEE", "6-FFFFFFF", "7-FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", "8-DDDDDD", "9-EEEEEE","10-CCCCCC", "11-DDDDDD", "12-EEEEEE")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preIndex = 0 //如果有需要默认显示第一个；
        val preText = testList[0]
        drop_down_top.setDropDownAdapter(
            list = testList,
            preText = preText,
            autoSelected = true,
            autoSelectedPosition = preIndex,
            onSelectionDisplay = { textView: TextView, i: Int ->
                // TODO 显示下拉菜单选项的文本，放到这里是为了更好的自定义显示
                textView.text = testList[i]
            },
            onSelected = {
                if (it != -1) {
                    // Set selected shot admin observable
                    Toast.makeText(this, "你选择了"+ testList[it], Toast.LENGTH_LONG).show()
                }
            }
        )


        val preIndex2 = -1 //如果不需要默认显示第一个；
        drop_down_bottom.setDropDownAdapter(
            list = testList,
            preText = "请选择》",
            autoSelected = false,
            autoSelectedPosition = preIndex2,
            onSelectionDisplay = { textView: TextView, i: Int ->
                // TODO 显示下拉菜单选项的文本，放到这里是为了更好的自定义显示
                textView.text = testList[i]
            },
            onSelected = {
                if (it != -1) {
                    // Set selected shot admin observable
                    Toast.makeText(this, "你选择了"+ testList[it], Toast.LENGTH_LONG).show()
                }
            },
            options = 2
        )
    }
}