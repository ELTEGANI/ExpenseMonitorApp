package com.expensemoitor.expensemonitor.utilites

import android.annotation.SuppressLint
import android.util.Log
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import java.text.SimpleDateFormat
import java.util.*






@SuppressLint("SimpleDateFormat")
fun getCurrentDate():String {
    val date = Calendar.getInstance().time
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
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
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    calendar.add(Calendar.DATE, 6)
    val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    return "$startDate*$endDate"
}



fun getTheStartAndTheEndOfTheMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, 0)
    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
    val monthFirstDay = calendar.time
    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    val monthLastDay = calendar.time

    val startDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(monthFirstDay)
    val endDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(monthLastDay)

    return "$startDateStr*$endDateStr"
}


fun checkIfDurationFinished(){
    //today
    val savedCurrentDate = PrefManager.getCurrentDate(context)

    //week
    val weekDates = getStartAndEndOfTheWeek().split("*")
    val endOfWeek = weekDates[1]


    //month
    val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
    val endOfMonth = monthDates[1]

    //compare dates
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())

        val savedDate = sdf.parse(savedCurrentDate)
        val currentDate = sdf.parse(getCurrentDate())
        val endOfTheWeek = sdf.parse(endOfWeek)
        val endOfTheMonth = sdf.parse(endOfMonth)


        if (currentDate.compareTo(savedDate) > 0){
            PrefManager.saveUpdatedTodayExpense(context,0)
            PrefManager.saveCurrentDate(context, getCurrentDate())
        }

        if (currentDate.compareTo(endOfTheWeek) > 0){
            PrefManager.saveUpdatedWeekExpense(context,0)
        }

        if(currentDate.compareTo(endOfTheMonth) > 0){
            PrefManager.saveUpdatedMonthExpense(context,0)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}