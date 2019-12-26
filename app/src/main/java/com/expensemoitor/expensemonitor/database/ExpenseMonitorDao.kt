package com.expensemoitor.expensemonitor.database

import androidx.room.Dao
import androidx.room.Insert


@Dao
interface ExpenseMonitorDao {
    @Insert
    fun insert(expenses: UserExpenses)
}