package com.monitoryourexpenses.expenses.network

import com.monitoryourexpenses.expenses.database.Expenses
import com.squareup.moshi.Json
import java.math.BigDecimal


data class ExpenseData(
    val amount: String,
    val description:String,
    val date:String,
    val currency:String,
    val category:String
)


data class ExpenseResponseMsg (
    @Json(name = "message")
    var message: String?,
    @Json(name = "expense")
    var expense: Expense?
)

data class DeleteAndUpdateResponse(
    val message:String
)


data class Expense (
    @Json(name = "id")
    var id: String? ,
    @Json(name = "amount")
    var amount: BigDecimal? ,
    @Json(name = "description")
    var description: String? ,
    @Json(name = "expenseCategory")
    var expenseCategory: String? ,
    @Json(name = "currency")
    var currency: String? ,
    @Json(name = "date")
    var date: String? ,
    @Json(name = "userId")
    var userId: String? ,
    @Json(name = "updatedAt")
    var updatedAt: String? ,
    @Json(name = "createdAt")
    var createdAt: String?
)


data class AllExpense (
    @Json(name = "id")
    var id: String? ,
    @Json(name = "amount")
    var amount: BigDecimal? ,
    @Json(name = "description")
    var description: String? ,
    @Json(name = "expenseCategory")
    var expenseCategory: String? ,
    @Json(name = "currency")
    var currency: String? ,
    @Json(name = "date")
    var date: String?
)