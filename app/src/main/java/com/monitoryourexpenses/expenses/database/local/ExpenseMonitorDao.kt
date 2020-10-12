package com.monitoryourexpenses.expenses.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.monitoryourexpenses.expenses.database.AllCategories
import com.monitoryourexpenses.expenses.database.AllCurrencies
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.Expenses
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface ExpenseMonitorDao {

    @Query("SELECT * from expenses WHERE date=:todayDate and currency =:currency")
    fun retrieveTodayExpense(todayDate: String, currency: String): LiveData<List<Expenses>>

    @Query("SELECT * from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    fun retrieveWeekExpense(startWeek: String, endWeek: String, currency: String): LiveData<List<Expenses>>

    @Query("SELECT * from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    fun retrieveMonthExpense(startMonth: String, endMonth: String, currency: String): LiveData<List<Expenses>>

    @Query("SELECT SUM(amount) from expenses WHERE date=:todayDate and currency =:currency")
    fun retrieveSumationOfTodayExpense(todayDate: String, currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    fun retrieveSumationOfWeekExpense(startWeek: String, endWeek: String, currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    fun retrieveSumationOfMonthExpense(startMonth: String, endMonth: String, currency: String): Flow<String>

    @Query("DELETE FROM expenses WHERE expense_id=:id")
    suspend fun deleteExpenses(id: String)

    // update expense
    @Query("UPDATE expenses SET amount=:amount,description=:description,expense_category=:expensecategory,currency=:currency,date=:date WHERE expense_id=:id")
    suspend fun updateExpenses(id: String, amount: BigDecimal, description: String, expensecategory: String, date: String, currency: String)

    @Insert
    suspend fun insertExpenses(expenses: Expenses)

    @Insert
    suspend fun insertNewCategory(categories: List<Categories>)

    @Insert
    fun insertExpensesForCachingData(vararg expenses: Expenses)

    @Query("SELECT SUM(amount) from expenses WHERE currency =:currency")
    suspend fun sumationOfSpecifiedExpenses(currency: String): String

    @Query("SELECT * from categories")
    fun selectAllCategories(): LiveData<List<Categories>>

    @Query("select currency,SUM(amount) as amount from expenses GROUP BY currency")
    fun selectSumationOfCurrencies(): LiveData<List<AllCurrencies>>

    @Query("select expense_category,SUM(amount) as amount from expenses WHERE date BETWEEN :startMonth AND :endMonth AND currency =:currency GROUP BY expense_category")
    fun selectSumationOfCategories(startMonth: String, endMonth: String, currency: String): LiveData<List<AllCategories>>



}
