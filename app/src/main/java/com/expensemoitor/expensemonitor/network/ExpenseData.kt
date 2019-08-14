package com.expensemoitor.expensemonitor.network




data class ExpenseData(
   val amount:String,
   val description:String,
   val date:String,
   val form:String,
   val category:String
)



data class expenseresponse(
    val message:String
)