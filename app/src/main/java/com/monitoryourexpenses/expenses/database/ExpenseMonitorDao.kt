package com.monitoryourexpenses.expenses.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseMonitorDao {

    @Query("SELECT * from expenses WHERE date=:todayDate and currency =:currency")
    fun retrieveTodayExpense(todayDate:String,currency: String): LiveData<List<Expenses>>

    @Query("SELECT * from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    fun retrieveWeekExpense(startWeek:String,endWeek:String,currency: String):LiveData<List<Expenses>>

    @Query("SELECT * from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    fun retrieveMonthExpense(startMonth:String,endMonth:String,currency:String): LiveData<List<Expenses>>

    @Query("SELECT SUM(amount) from expenses WHERE date=:todayDate and currency =:currency")
    fun retrieveSumationOfTodayExpense(todayDate:String,currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
    fun retrieveSumationOfWeekExpense(startWeek:String,endWeek:String,currency: String): Flow<String>

    @Query("SELECT SUM(amount) from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
    fun retrieveSumationOfMonthExpense(startMonth:String,endMonth:String,currency:String): Flow<String>

    @Query("DELETE FROM expenses WHERE id=:id")
    suspend fun deleteExpenses(id:String)

    //update expense
    @Query("UPDATE expenses SET amount=:amount,description=:description,expense_category=:expensecategory,date=:date WHERE id=:id")
    suspend fun updateExpenses(id: String,amount:String,description:String,expensecategory:String,date:String)

    @Query("DELETE  FROM expenses WHERE date between :startMonth and :endMonth")
    suspend fun clearAllMonthExpenses(startMonth:String,endMonth:String)

    @Insert
    suspend fun insertExpenses(expenses: Expenses)

    @Insert
    suspend fun insertNewCategory(categories:Categories)

    @Insert
    fun insertExpensesForCachingData(vararg expenses: Expenses)

    @Query("SELECT * from expenses")
    fun selectAllExpenses(): List<Expenses>

    @Query("SELECT SUM(amount) from expenses WHERE currency =:currency")
    suspend fun sumationOfSpecifiedExpenses(currency: String): String

}