package com.monitoryourexpenses.expenses.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.monitoryourexpenses.expenses.network.ApiFactory
import com.monitoryourexpenses.expenses.network.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class LocalRepository(private val database:ExpenseMonitorDao) {


    fun getTodayExpenses(todayDate:String, currency: String): LiveData<List<Expenses>> {
        return database.retrieveTodayExpense(todayDate,currency)
    }

    fun getWeekExpenses(startWeek:String,endWeek:String,currency: String): LiveData<List<Expenses>> {
        return database.retrieveWeekExpense(startWeek,endWeek,currency)
    }

    fun getMonthExpenses(startMonth:String, endMonth:String, currency: String): LiveData<List<Expenses>> {
        return  database.retrieveMonthExpense(startMonth,endMonth,currency)
    }

      fun getSumationOfTodayExpenses(todayDate:String, currency: String): Flow<String> {
        return database.retrieveSumationOfTodayExpense(todayDate,currency)
     }

      fun getSumationOfMonthExpenses(startMonth:String, endMonth:String, currency: String):Flow<String>{
          return database.retrieveSumationOfMonthExpense(startMonth,endMonth,currency)
    }

     fun getSumationOfWeekExpenses(startWeek:String, endWeek:String, currency: String):Flow<String>{
         return database.retrieveSumationOfWeekExpense(startWeek,endWeek,currency)
    }


    suspend fun clearMonthExpenses(startMonth: String,endMonth: String){
       database.clearAllMonthExpenses(startMonth,endMonth)
    }


    suspend fun deleteExpneseUsingId(id:String){
        database.deleteExpenses(id)
    }


    suspend fun updateExpenseUsingId(expense_id: String,amount:String,description:String,expensecategory:String,date:String){
        database.updateExpenses(expense_id,amount,description,expensecategory,date)
    }


    suspend fun insertExpense(expenses: Expenses){
        database.insertExpenses(expenses)
    }


    suspend fun getAllExpensesFromServer(startMonth: String,endMonth: String){
        withContext(Dispatchers.IO){
            if(database.selectAllExpenses().size == 0) {
                val duration = Duration(startMonth,endMonth)
                try {
                    try {
                        val expensesList = ApiFactory.ALL_EXPENSES.allExpensesAsync(duration).await()
                        database.insertExpensesForCachingData(*expensesList.toTypedArray())
                    }catch (http:HttpException){
                      Log.d("http",http.toString())
                    }
                }catch (t:Throwable){
                    Log.d("t",t.toString())
                }
            }
        }
    }


}