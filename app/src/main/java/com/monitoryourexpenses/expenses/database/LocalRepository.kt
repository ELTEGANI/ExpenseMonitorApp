package com.monitoryourexpenses.expenses.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


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

    suspend fun insertNewCategory(categories: List<Categories>){
        database.insertNewCategory(categories)
    }

    suspend fun sumationOfSpecifiedExpenses(currency: String):String{
       return database.sumationOfSpecifiedExpenses(currency)
    }

    fun getAllCategories(): LiveData<List<Categories>> {
        return database.selectAllCategories()
    }

    fun selectSumationOfCurrencies(): LiveData<List<AllCurrencies>> {
        return database.selectSumationOfCurrencies()
    }

    fun selectSumationOfCatogries(startMonth: String,endMonth: String,currency:String): LiveData<List<AllCategories>> {
        return database.selectSumationOfCategories(startMonth,endMonth,currency)
    }
}