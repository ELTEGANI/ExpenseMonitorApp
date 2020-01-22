package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monitoryourexpenses.expenses.network.DurationExpenseResponse

class UpdateAndDeleteFragmentViewModelFactory(private val durationExpenseResponse: DurationExpenseResponse, private val application: Application):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateAndDeleteFragmentViewModel::class.java)) {
            return UpdateAndDeleteFragmentViewModel(durationExpenseResponse, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
