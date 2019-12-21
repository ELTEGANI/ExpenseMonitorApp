package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CreateExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    fun createNewExpense(@Body expenseData: ExpenseData) : Deferred<ExpenseResponseMsg>

}


interface RegisterationService {
    @POST(AppConstants.USER_REGISTERATION)
    fun registerationUser(@Body userData: UserData): Deferred<RegisterationResponse>
}


interface GetExpenseBasedOnDurationsService {
    @POST(AppConstants.GET_EXPENSES_BASED_ON_DURATION)
    fun getExpensesBasedOnDuration(@Body durationTag: DurationTag): Deferred<List<ExpensesResponse>>
}

interface DeleteExpenseService{
    @DELETE(AppConstants.DELETE_EXPENSE)
    fun deleteExpense(@Path("expenseid")expenseid:String):Deferred<DeleteAndUpdateResponse>
}


interface UpdateExpenseService{
    @PUT(AppConstants.UPDATE_EXPENSE)
    fun updateExpense(@Path("expenseid")expenseid:String,@Body expenseData: ExpenseData):Deferred<DeleteAndUpdateResponse>
}



