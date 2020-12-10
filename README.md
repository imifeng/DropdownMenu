# DropdownMenu
自定义下拉菜单选择-封装 PopupWindow（确定弹出位置） + CardView（设置弹框背景阴影效果）+ RecyclerView（数据列表显示）

* 宽度需要设定，不然列表项的宽度只能适应已显示的最大宽度项

运行效果：
<br/>
<img src="https://img-blog.csdnimg.cn/20201210101600915.gif" width = "405" height = "720" />



代码中使用：
``` 
drop_down_view.setDropDownAdapter(
            list = testList,  /* 下拉菜单选项的数据 */
            preText = "请选择",  /* 预显示文本 */
            autoSelected = false, /* 是否存在默认选择 */
            autoSelectedPosition = preIndex2, /* 存在默认选择的位置 */
            onSelectionDisplay = { selection: TextView, i: Int ->  /* 显示下拉菜单选项的文本 */
                // TODO 显示下拉菜单选项的文本，放到这里是为了更好的自定义显示
                selection.text = testList[i]
            },
            onSelected = { /* 下拉菜单选中后回调 */
                // TODO 
                if (it != -1) {
                    Toast.makeText(this, "你选择了"+ testList[it], Toast.LENGTH_LONG).show()
                }
            },
            options = 2  /* 下拉菜单列表可见项，即设置该项可以控制下拉菜单的高度 */
        )
```

XML使用：
```
<com.android.dropdownmenu.dropdown.DropDownLayoutView
        android:id="@+id/drop_down_view"
        android:layout_width="200dp"
        android:layout_height="40dp"
        android:layout_marginStart="70dp"
        android:layout_marginEnd="30dp"
        app:innerTextColor="@color/primary_black" /* 下拉菜单中设置显示的字体颜色 */
        app:innerTextSize="14sp"      /* 下拉菜单中设置显示的字体大小 */
        app:innerOffsetX="45dp"       /* 下拉菜单X轴偏移距离，用来调整弹出位置 */
        app:innerOffsetY="25dp"       /* 下拉菜单Y轴偏移距离，用来调整弹出位置 */
        app:innerMenuWidth="300dp"    /* 下拉菜单宽度 */
        app:innerVisibleOptions="3"   /* 下拉菜单列表可见项，即设置该项可以控制下拉菜单的高度 */
        app:innerItemHeight="60dp"    /* 下拉菜单列表单项的高度 */
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/text_bottom" />
```
