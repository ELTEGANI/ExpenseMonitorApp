package com.expensemoitor.expensemonitor.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class DurationTag(
    val duration:String,
    val startDate: String?,
    val endDate: String?
)



@Parcelize
data class ExpensesResponse(
    val id :String,
    val amount :String,
    val description:String,
    val expenseCategory:String,
    val date:String):Parcelable