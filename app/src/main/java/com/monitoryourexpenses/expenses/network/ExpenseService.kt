package com.monitoryourexpenses.expenses.network

import com.monitoryourexpenses.expenses.utilites.AppConstants
import retrofit2.http.Body
import retrofit2.http.POST


interface ExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    suspend fun createNewExpenseAsync(@Body expenseData: ExpenseData) : ExpenseResponseMsg
}