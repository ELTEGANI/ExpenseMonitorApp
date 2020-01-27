package com.monitoryourexpenses.expenses.network

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


data class DurationTag(
    val duration:String,
    val currency:String,
    val startDate: String?,
    val endDate: String?
)

@Keep
@Parcelize
data class DurationExpenseResponse (
    var id: String? ,
    var amount: String? ,
    var description: String? ,
    var expenseCategory: String? ,
    var currency: String?,
    var date: String?
): Parcelable


