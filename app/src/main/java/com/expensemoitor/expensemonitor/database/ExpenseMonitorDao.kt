package com.expensemoitor.expensemonitor.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


@Dao
interface ExpenseMonitorDao {

    @Insert
     suspend fun insertExpense(userExpenses: UserExpenses)


    @Query("UPDATE user_expense_table SET today_expenses=:todayExpense WHERE currency =:currency")
    suspend fun updateTodayExpenses(todayExpense: BigDecimal, currency:String)


    @Query("UPDATE user_expense_table SET week_expenses=:weekExpense WHERE currency =:currency")
    suspend fun updateWeekExpenses(weekExpense: BigDecimal, currency:String)


    @Query("UPDATE user_expense_table SET month_expenses=:monthExpenses WHERE currency =:currency")
    suspend fun updateMonthExpenses(monthExpenses: BigDecimal, currency:String)


    @Query("SELECT today_expenses from user_expense_table WHERE currency =:currency")
    fun retrieveTodayExpense(currency: String): Flow<String>


    @Query("SELECT week_expenses from user_expense_table WHERE currency =:currency")
    fun retrieveWeekExpense(currency: String): Flow<String>


    @Query("SELECT month_expenses from user_expense_table WHERE currency =:currency")
    fun retrieveMonthExpense(currency: String): Flow<String>

    @Query("SELECT currency from user_expense_table WHERE currency =:currency")
    suspend fun checkCurrencyExistence(currency: String): String


}