package com.expensemoitor.expensemonitor.todayexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class TodayExpenseFragmentViewModelFactory (private val database:ExpenseMonitorDao,private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayExpenseFragmentViewModel::class.java)) {
            return TodayExpenseFragmentViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

