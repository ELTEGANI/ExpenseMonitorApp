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


    //select for tab list
//    @Query("SELECT * from expenses WHERE date=:todayDate and currency =:currency")
//    suspend fun retrieveTodayExpense(todayDate:String,currency: String): LiveData<List<String>>

//    @Query("SELECT * from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
//    suspend fun retrieveWeekExpense(startWeek:String,endWeek:String,currency: String): LiveData<List<String>>
//
//    @Query("SELECT * from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
//    suspend fun retrieveMonthExpense(startMonth:String,endMonth:String,currency:String): LiveData<List<String>>


    //select for sum amount
//    @Query("SELECT SUM(amount) from expenses WHERE date=:todayDate and currency =:currency")
//    suspend fun retrieveSumationOfTodayExpense(todayDate:String,currency: String): Flow<String>
//
//    @Query("SELECT SUM(amount) from expenses WHERE date between :startWeek and :endWeek and currency =:currency")
//    suspend fun retrieveSumationOfWeekExpense(startWeek:String,endWeek:String,currency: String): Flow<String>
//
//    @Query("SELECT SUM(amount) from expenses WHERE date between :startMonth and :endMonth and currency =:currency")
//    suspend fun retrieveSumationOfMonthExpense(startMonth:String,endMonth:String,currency:String): Flow<String>
//
//    //delete expense
//    @Query("DELETE FROM expenses WHERE expense_id=:id")
//    suspend fun deleteExpenses(id:String)


    //update expense
//    @Query("UPDATE FROM expenses WHERE expense_id=:id")
//    suspend fun updateExpenses(id: String)

    //clear per duration
//    @Query("DELETE  FROM expenses WHERE date=:today")
//    suspend fun clearAllTodayExpenses(today:String)
//
//    @Query("DELETE  FROM expenses WHERE date between :startWeek and :endWeek")
//    suspend fun clearAllWeekExpenses(startWeek:String,endWeek:String)
//
//    @Query("DELETE  FROM expenses WHERE date between :startMonth and :endMonth")
//    suspend fun clearAllMonthExpenses(startMonth:String,endMonth:String)


    //insert when create expenses
    @Insert
    suspend fun insertExpenses(expenses: Expenses)



}