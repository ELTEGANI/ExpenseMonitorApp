package com.expensemoitor.expensemonitor.registeruser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class RegisterationUserViewModelFactory (private val dataBase: ExpenseMonitorDao, private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterationUserViewModel::class.java)) {
            return RegisterationUserViewModel(dataBase,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

