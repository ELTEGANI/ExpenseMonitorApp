package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface ExpenseUrls {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    fun createNewExpense(@Body expenseData: ExpenseData) : Deferred<expenseresponse>

}