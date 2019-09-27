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



fun expenseFormat(amount:String): String {
    var amountFormatted= ""
    try {
        val value = amount.replace(",", "")
        val reverseValue = StringBuilder(value).reverse()
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


fun getStartAndEndOfTheWeek():String{
    val calendar = Calendar.getInstance()

    val date1 = calendar.time
    val checkformate = SimpleDateFormat("MM/yyyy")
    val currentCheckdate = checkformate.format(date1)

    val weekn = calendar.get(Calendar.WEEK_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    calendar.clear()
    calendar.firstDayOfWeek = Calendar.SATURDAY
    calendar.set(Calendar.WEEK_OF_MONTH, weekn)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    val datef = calendar.time
    val time = calendar.timeInMillis + 518400000L
    val dateL = Date(time)
    var firtdate = simpleDateFormat.format(datef)
    var lastdate = simpleDateFormat.format(dateL)
    val firtdateCheck = checkformate.format(datef)
    val lastdateCheck = checkformate.format(dateL)


    if (!firtdateCheck.toString().equals(currentCheckdate, ignoreCase = true)) {
        firtdate = calendar.get(Calendar.YEAR).toString() + "-" + calendar.get(Calendar.MONTH) + "-" + "1"
    }

    if (!lastdateCheck.toString().equals(currentCheckdate, ignoreCase = true)) {
        val ma = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        lastdate = calendar.get(Calendar.YEAR).toString() + "-" + calendar.get(Calendar.MONTH) + "-" + ma.toString()
    }

    val endDate = lastdate.toString()
    val startDate = firtdate.toString()

    return "$startDate*$endDate"
}