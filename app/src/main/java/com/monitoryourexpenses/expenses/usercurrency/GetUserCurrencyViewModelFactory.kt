package com.monitoryourexpenses.expenses.usercurrency

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class GetUserCurrencyViewModelFactory(var database: ExpenseMonitorDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetUserCurrencyUserViewModel::class.java)) {
            return GetUserCurrencyUserViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
