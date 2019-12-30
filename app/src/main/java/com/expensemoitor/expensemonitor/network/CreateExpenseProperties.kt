package com.expensemoitor.expensemonitor.network

import java.math.BigDecimal


data class ExpenseData(
    val amount: BigDecimal,
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