package com.monitoryourexpenses.expenses.database

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

class LocalRepository @Inject constructor(private val expenseMonitorDao: ExpenseMonitorDao,
 var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences) {

    fun getTodayExpenses(): LiveData<List<Expenses>>? {
        return expenseMonitorDao.retrieveTodayExpense(expenseMonitorSharedPreferences.getCurrentDate().toString(),
            expenseMonitorSharedPreferences.getCurrency().toString())
    }

    fun getWeekExpenses(startWeek: String, endWeek: String, currency: String): LiveData<List<Expenses>> {
        return expenseMonitorDao.retrieveWeekExpense(startWeek, endWeek, currency)
    }

     fun getMonthExpenses(startMonth: String, endMonth: String, currency: String): LiveData<List<Expenses>> {
        return expenseMonitorDao.retrieveMonthExpense(startMonth, endMonth, currency)
    }

       fun getSumationOfTodayExpenses(todayDate: String, currency: String): Flow<String> {
        return expenseMonitorDao.retrieveSumationOfTodayExpense(todayDate, currency)
     }

       fun getSumationOfMonthExpenses(startMonth: String, endMonth: String, currency: String): Flow<String> {
          return expenseMonitorDao.retrieveSumationOfMonthExpense(startMonth, endMonth, currency)
    }

      fun getSumationOfWeekExpenses(startWeek: String, endWeek: String, currency: String): Flow<String> {
         return expenseMonitorDao.retrieveSumationOfWeekExpense(startWeek, endWeek, currency)
    }

     suspend fun deleteExpenseUsingId(id: String) {
        expenseMonitorDao.deleteExpenses(id)
    }

     suspend fun updateExpenseUsingId(expense_id: String, amount: BigDecimal, description: String, expensecategory: String, date: String
     ,currency: String) {
        expenseMonitorDao.updateExpenses(expense_id,amount,description,expensecategory,date,currency)
    }

     suspend fun insertExpense(expenses: Expenses) {
        expenseMonitorDao.insertExpenses(expenses)
    }

     suspend fun insertNewCategory(categories: List<Categories>) {
        expenseMonitorDao.insertNewCategory(categories)
    }

    suspend fun totalOfCurrentExpenses() : Boolean{
        val totalExpense = expenseMonitorDao.sumationOfSpecifiedExpenses(expenseMonitorSharedPreferences.getCurrency().toString())
        if(expenseMonitorSharedPreferences.getExceedExpense() != null) {
           return  totalExpense == expenseMonitorSharedPreferences.getExceedExpense()
        }
        return false
    }

     fun getAllCategories(): LiveData<List<Categories>> {
        return expenseMonitorDao.selectAllCategories()
    }

     fun selectSumationOfCurrencies(): LiveData<List<AllCurrencies>> {
        return expenseMonitorDao.selectSumationOfCurrencies()
    }

     fun selectSumationOfCategories(startMonth: String, endMonth: String, currency: String): LiveData<List<AllCategories>> {
        return expenseMonitorDao.selectSumationOfCategories(startMonth, endMonth, currency)
    }


}
