package com.expensemoitor.expensemonitor.network




data class ExpenseData(
   val amount:String,
   val description:String,
   val date:String,
   val category:String
)



data class ExpenseResponseMsg(
    val message:String,
    val Expense:Int
)


data class DeleteAndUpdateResponse(
    val message:String
)