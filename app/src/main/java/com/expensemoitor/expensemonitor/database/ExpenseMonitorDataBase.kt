package com.expensemoitor.expensemonitor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expensemoitor.expensemonitor.utilites.Converter


@Database(entities = [UserExpenses::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)

abstract class ExpenseMonitorDataBase : RoomDatabase() {
    abstract val expenseMonitorDao: ExpenseMonitorDao
    companion object {
        @Volatile
        private var INSTANCE: ExpenseMonitorDataBase? = null
        fun getInstance(context: Context): ExpenseMonitorDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExpenseMonitorDataBase::class.java,
                        "expense_monitor_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
