package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses

class UpdateAndDeleteFragmentViewModelFactory(private val expenses: Expenses,private val application: Application,private val dataBase: ExpenseMonitorDao):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateAndDeleteFragmentViewModel::class.java)) {
            return UpdateAndDeleteFragmentViewModel(expenses,application,dataBase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
