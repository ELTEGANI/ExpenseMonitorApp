package com.monitoryourexpenses.expenses.database

import androidx.lifecycle.LiveData
import com.monitoryourexpenses.expenses.database.local.ExpenseMonitorDao
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
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

     suspend fun sumationOfSpecifiedExpenses(currency: String): String {
       return expenseMonitorDao.sumationOfSpecifiedExpenses(currency)
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
