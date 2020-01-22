package com.monitoryourexpenses.expenses.monthexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class MonthExpenseFragmentViewModelFactory(private val database: ExpenseMonitorDao, private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthExpenseFragmentViewModel::class.java)) {
            return MonthExpenseFragmentViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
