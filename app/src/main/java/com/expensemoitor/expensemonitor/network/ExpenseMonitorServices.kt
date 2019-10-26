package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    fun createNewExpense(@Body expenseData: ExpenseData) : Deferred<expenseResponse>

}


interface RegisterationService {
    @POST(AppConstants.USER_REGISTERATION)
    fun registerationUser(@Body userData: UserData): Deferred<RegisterationResponse>
}


interface GetExpenseBasedOnDurationsService{
    @POST(AppConstants.GET_EXPENSES_BASED_ON_DURATION)
    fun getExpensesBasedOnDuration(@Body durationTag: DurationTag):Deferred<List<ExpensesResponse>>
}

