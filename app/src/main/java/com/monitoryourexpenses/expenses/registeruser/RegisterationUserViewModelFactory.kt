package com.monitoryourexpenses.expenses.registeruser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.data.UserRepository
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import java.lang.IllegalArgumentException

class RegisterationUserViewModelFactory (private val userRepository: UserRepository,var database:ExpenseMonitorDao,private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterationUserViewModel::class.java)) {
            return RegisterationUserViewModel(userRepository,database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

