package com.monitoryourexpenses.expenses.data

import com.monitoryourexpenses.expenses.api.ExpenseData
import com.monitoryourexpenses.expenses.api.ExpenseResponseMsg
import com.monitoryourexpenses.expenses.api.RegisterationResponse
import com.monitoryourexpenses.expenses.api.UserData
import com.monitoryourexpenses.expenses.utilites.AppConstants
import retrofit2.http.Body
import retrofit2.http.POST


interface ExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    suspend fun createNewExpense(@Body expenseData: ExpenseData) : ExpenseResponseMsg


    @POST(AppConstants.USER_REGISTERATION)
    suspend fun createNewUser(@Body userData: UserData) : RegisterationResponse

}