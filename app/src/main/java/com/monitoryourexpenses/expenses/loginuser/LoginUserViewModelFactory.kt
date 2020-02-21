package com.monitoryourexpenses.expenses.loginuser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class LoginUserViewModelFactory (private val application: Application,private val database: ExpenseMonitorDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginUserViewModel::class.java)) {
            return LoginUserViewModel(application,database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

