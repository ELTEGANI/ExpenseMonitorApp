package com.monitoryourexpenses.expenses.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Duration(
    val startDate: String?,
    val endDate: String?
)


@Parcelize
data class DurationExpenseResponse (
    var id: String? ,
    var amount: String? ,
    var description: String? ,
    var expenseCategory: String? ,
    var currency: String?,
    var date: String?
): Parcelable


