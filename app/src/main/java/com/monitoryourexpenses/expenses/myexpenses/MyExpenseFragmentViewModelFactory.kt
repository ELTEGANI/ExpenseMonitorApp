package com.monitoryourexpenses.expenses.myexpenses

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.data.ExpensesRepository
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MyExpenseFragmentViewModelFactory(private val dataBase: ExpenseMonitorDao,private val expensesRepository: ExpensesRepository,private val application: Application):ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyExpenseFragmentViewModel::class.java)) {
            return MyExpenseFragmentViewModel(dataBase,expensesRepository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}