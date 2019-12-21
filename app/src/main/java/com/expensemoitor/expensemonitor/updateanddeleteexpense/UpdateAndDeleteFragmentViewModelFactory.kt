package com.expensemoitor.expensemonitor.updateanddeleteexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.expensemoitor.expensemonitor.network.ExpensesResponse

class UpdateAndDeleteFragmentViewModelFactory(private val expenseResponse: ExpensesResponse, private val application: Application):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateAndDeleteFragmentViewModel::class.java)) {
            return UpdateAndDeleteFragmentViewModel(expenseResponse, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
