package com.monitoryourexpenses.expenses.network



data class ExpenseData(
    val amount: String,
    val description:String,
    val date:String,
    val currency:String,
    val category:String
)


data class ExpenseResponseMsg(
    val message:String
)

data class DeleteAndUpdateResponse(
    val message:String
)