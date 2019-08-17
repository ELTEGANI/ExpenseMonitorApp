package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateExpenseUrls {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    fun createNewExpense(@Body expenseData: ExpenseData) : Deferred<expenseresponse>

}


interface RegisterationUrls {
    @POST(AppConstants.USER_REGISTERATION)
    fun registerationUser(@Body userData: UserData): Deferred<RegisterationResponse>
}