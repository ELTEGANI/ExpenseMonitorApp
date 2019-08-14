package com.expensemoitor.expensemonitor.utilites

import java.text.SimpleDateFormat
import java.util.*


fun displayCurrentDate():String {
    val calendar = Calendar.getInstance()
    val mdformat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate = mdformat.format(calendar.time)
    return  currentDate
}

