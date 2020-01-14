package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CreateExpenseService {
    @POST(AppConstants.CREATE_NEW_EXPENSE)
    fun createNewExpenseAsync(@Body expenseData: ExpenseData) : Deferred<ExpenseResponseMsg>

}


interface RegisterationService {
    @POST(AppConstants.USER_REGISTERATION)
    fun registerationUserAsync(@Body userData: UserData): Deferred<RegisterationResponse>
}


interface GetTodayExpenseService {
    @POST(AppConstants.GET_EXPENSES_BASED_ON_DURATION)
    fun getdurationExpensesAsync(@Body durationTag: DurationTag): Deferred<List<DurationExpenseResponse>>
}


interface DeleteExpenseService{
    @DELETE(AppConstants.DELETE_EXPENSE)
    fun deleteExpenseAsync(@Path("expenseid")expenseid:String):Deferred<DeleteAndUpdateResponse>
}


interface UpdateExpenseService{
    @PUT(AppConstants.UPDATE_EXPENSE)
    fun updateExpenseAsync(@Path("expenseid")expenseid:String, @Body expenseData: ExpenseData):Deferred<DeleteAndUpdateResponse>
}


