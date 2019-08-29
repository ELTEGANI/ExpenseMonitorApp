package com.expensemoitor.expensemonitor.utilites

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun displayCurrentDate():String {
    val calendar = Calendar.getInstance()
    val dayformat = SimpleDateFormat("yyyy-MM-dd")
    return dayformat.format(calendar.time)
}

