package com.monitoryourexpenses.expenses.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monitoryourexpenses.expenses.utilites.Converter

@Database(entities = [Expenses::class, Categories::class], version = 7, exportSchema = false)
@TypeConverters(Converter::class)

abstract class ExpenseMonitorDataBase : RoomDatabase() {
    abstract val expenseMonitorDao: ExpenseMonitorDao
}
