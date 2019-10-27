package com.expensemoitor.expensemonitor.utilites

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*










@SuppressLint("SimpleDateFormat")
fun displayCurrentDate():String {
    val calendar = Calendar.getInstance()
    val dayformat = SimpleDateFormat("yyyy-MM-dd")
    return dayformat.format(calendar.time)
}



fun expenseFormat(amount: String?): String {
    var amountFormatted= ""
    try {
        val value = amount?.replace(",", "")
        val reverseValue = StringBuilder(value.toString()).reverse()
            .toString()
        val finalValue = StringBuilder()
        for (i in 1..reverseValue.length)
        {
            val `val` = reverseValue[i - 1]
            finalValue.append(`val`)
            if (i % 3 == 0 && i != reverseValue.length && i > 0)
            {
                finalValue.append(",")
            }
        }
        amountFormatted = finalValue.reverse().toString()
    } catch (e: Exception) {
        // Do nothing since not a number
    }
    return amountFormatted
}



fun getStartAndEndOfTheWeek():String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val startDate = df.format(calendar.getTime())
    calendar.add(Calendar.DATE, 6)
    val endDate = df.format(calendar.time)

    return "$startDate*$endDate"
}



fun getTheStartAndTheEndOfTheMonth():String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, 0)
    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
    val monthFirstDay = calendar.time
    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    val monthLastDay = calendar.time

    val df = SimpleDateFormat("yyyy-MM-dd",Locale.US)
    val startDateStr = df.format(monthFirstDay)
    val endDateStr = df.format(monthLastDay)

    Log.e("DateFirstLast", "$startDateStr $endDateStr")
    return "$startDateStr*$endDateStr"
}


