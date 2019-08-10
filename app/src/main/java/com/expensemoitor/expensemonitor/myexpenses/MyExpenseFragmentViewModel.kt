package com.expensemoitor.expensemonitor.myexpenses

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyExpenseFragmentViewModel(application: Application) : ViewModel() {


    private val _navigateToMyExpense = MutableLiveData<Boolean>()
            val navigateToMyExpense : LiveData<Boolean>
             get() = _navigateToMyExpense


    fun onFabClicked(){
       _navigateToMyExpense.value = true
    }


    fun onNavigatedToMyExpense(){
        _navigateToMyExpense.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }


}