package com.monitoryourexpenses.expenses.network

import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.utilites.AppConstants
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

interface AllExpensesService{
    @POST(AppConstants.ALL_EXPENSES)
    fun allExpensesAsync(@Body duration: Duration): Deferred<List<Expenses>>
}

interface DeleteExpenseService{
    @DELETE(AppConstants.DELETE_EXPENSE)
    fun deleteExpenseAsync(@Path("expenseid")expenseid:String):Deferred<DeleteAndUpdateResponse>
}


interface UpdateExpenseService{
    @PUT(AppConstants.UPDATE_EXPENSE)
    fun updateExpenseAsync(@Path("expenseid")expenseid:String, @Body expenseData: ExpenseData):Deferred<DeleteAndUpdateResponse>
}


