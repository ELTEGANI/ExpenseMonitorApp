package com.expensemoitor.expensemonitor.network




data class DurationTag(
    val duration :String
)



data class GetExpensesResponse(
    val id :String,
    val amount :String,
    val description:String,
    val expenseCategory:String,
    val date:String
)