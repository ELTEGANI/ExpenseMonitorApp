package com.monitoryourexpenses.expenses.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val expenseMonitorDao: ExpenseMonitorDao) {

    fun getTodayExpenses(todayDate: String, currency: String): LiveData<List<Expenses>> {
        return expenseMonitorDao.retrieveTodayExpense(todayDate, currency)
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

    suspend fun deleteExpneseUsingId(id: String) {
        expenseMonitorDao.deleteExpenses(id)
    }

    suspend fun updateExpenseUsingId(expense_id: String, amount: String, description: String, expensecategory: String, date: String) {
        expenseMonitorDao.updateExpenses(expense_id, amount, description, expensecategory, date)
    }

    suspend fun insertExpense(expenses: Expenses) {
        expenseMonitorDao.insertExpenses(expenses)
    }

    suspend fun insertNewCategory(categories: List<Categories>) {
        expenseMonitorDao.insertNewCategory(categories)
    }

    suspend fun sumationOfSpecifiedExpenses(currency: String): String {
       return expenseMonitorDao.sumationOfSpecifiedExpenses(currency)
    }

    fun getAllCategories(): LiveData<List<Categories>> {
        return expenseMonitorDao.selectAllCategories()
    }

    fun selectSumationOfCurrencies(): LiveData<List<AllCurrencies>> {
        return expenseMonitorDao.selectSumationOfCurrencies()
    }

    fun selectSumationOfCatogries(startMonth: String, endMonth: String, currency: String): LiveData<List<AllCategories>> {
        return expenseMonitorDao.selectSumationOfCategories(startMonth, endMonth, currency)
    }
}
