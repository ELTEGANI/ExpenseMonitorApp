package com.expensemoitor.expensemonitor.loginuser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class LoginUserViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginUserViewModel::class.java)) {
            return LoginUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

