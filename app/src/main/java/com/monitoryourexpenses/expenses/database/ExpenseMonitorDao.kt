package com.monitoryourexpenses.expenses.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


@Dao
interface ExpenseMonitorDao {


    @Query("SELECT * from expenses WHERE date=:todayDate and currency =:currency")
    suspend fun retrieveTodayExpense(todayDate:String,currency: String): List<Expenses>

    @Query("SELECT * from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    suspend fun retrieveWeekExpense(startWeek:String,endWeek:String,currency: String): List<Expenses>

    @Query("SELECT * from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    suspend fun retrieveMonthExpense(startMonth:String,endMonth:String,currency:String): List<Expenses>

    @Query("SELECT SUM(amount) from expenses WHERE date=:todayDate and currency =:currency")
    fun retrieveSumationOfTodayExpense(todayDate:String,currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    fun retrieveSumationOfWeekExpense(startWeek:String,endWeek:String,currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    fun retrieveSumationOfMonthExpense(startMonth:String,endMonth:String,currency:String): Flow<String>

    @Query("DELETE FROM expenses WHERE expense_id=:id")
    suspend fun deleteExpenses(id:String)

    //update expense
    @Query("UPDATE expenses SET amount=:amount,description=:description,expense_category=:expensecategory,date=:date WHERE expense_id=:expense_id")
    suspend fun updateExpenses(expense_id: String,amount:String,description:String,expensecategory:String,date:String)

    @Query("DELETE  FROM expenses WHERE date=:today")
    suspend fun clearAllTodayExpenses(today:String)

    @Query("DELETE  FROM expenses WHERE date between :startWeek and :endWeek")
    suspend fun clearAllWeekExpenses(startWeek:String,endWeek:String)

    @Query("DELETE  FROM expenses WHERE date between :startMonth and :endMonth")
    suspend fun clearAllMonthExpenses(startMonth:String,endMonth:String)

    @Insert
    suspend fun insertExpenses(expenses: Expenses)

    @Query("SELECT * from expenses WHERE date between :startMonth and :endMonth")
    suspend fun selectAllExpenses(startMonth:String,endMonth:String): List<Expenses>


}