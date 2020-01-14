package com.expensemoitor.expensemonitor.myexpenses

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao

class MyExpenseFragmentViewModelFactory(private val dataBase: ExpenseMonitorDao,private val application: Application):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyExpenseFragmentViewModel::class.java)) {
            return MyExpenseFragmentViewModel(dataBase,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}