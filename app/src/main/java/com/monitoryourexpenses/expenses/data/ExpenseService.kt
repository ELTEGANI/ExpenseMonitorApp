package com.monitoryourexpenses.expenses.data

import com.monitoryourexpenses.expenses.api.*
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface ExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    suspend fun createNewExpense(@Body expenseData: ExpenseData) : ExpenseResponseMsg


    @POST(AppConstants.USER_REGISTERATION)
    suspend fun createNewUser(@Body userData: UserData) : RegisterationResponse


    @PUT(AppConstants.UPDATE_EXPENSE)
    suspend fun updateExpense(@Path("expenseid")expenseid:String, @Body expenseData: ExpenseData) : DeleteAndUpdateResponse

    @DELETE(AppConstants.DELETE_EXPENSE)
    suspend fun deleteExpense(@Path("expenseid")expenseid:String) : DeleteAndUpdateResponse

    @GET(AppConstants.ALL_EXPENSES)
    suspend fun getAllExpenses(@Body duration: Duration):List<Expenses>

}