package com.expensemoitor.expensemonitor.network




data class DurationTag(
    val duration:String,
    val startDate: String?,
    val endDate: String?
)



data class ExpensesResponse(
    val id :String,
    val amount :String,
    val description:String,
    val expenseCategory:String,
    val date:String
)