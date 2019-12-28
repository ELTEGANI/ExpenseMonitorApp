package com.expensemoitor.expensemonitor.weekexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class WeekExpenseFragmentViewModelFactory(private val database: ExpenseMonitorDao, private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeekExpenseFragmentViewModel::class.java)) {
            return WeekExpenseFragmentViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

