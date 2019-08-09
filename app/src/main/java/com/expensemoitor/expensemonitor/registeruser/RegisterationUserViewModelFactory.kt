package com.expensemoitor.expensemonitor.registeruser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class RegisterationUserViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterationUserViewModel::class.java)) {
            return RegisterationUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

