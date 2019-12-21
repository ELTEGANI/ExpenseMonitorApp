package com.expensemoitor.expensemonitor.myexpenses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.checkIfDurationFinished


class MyExpenseFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext


    private val _navigateToMyExpense = MutableLiveData<Boolean>()
            val navigateToMyExpense : LiveData<Boolean>
             get() = _navigateToMyExpense

    val expense = MutableLiveData<String>()

    init {
        checkIfDurationFinished()
    }

    fun onFabClicked(){
       _navigateToMyExpense.value = true
    }


    fun onNavigatedToMyExpense(){
        _navigateToMyExpense.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }


    fun clearPrefs(){
        PrefManager.clear(application)
    }



}