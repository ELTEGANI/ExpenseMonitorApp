package com.expensemoitor.expensemonitor.myexpenses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.Converter.Companion.toBigDecimal
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*


class MyExpenseFragmentViewModel(val database:ExpenseMonitorDao,application: Application) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext
    private val _navigateToMyExpense = MutableLiveData<Boolean>()
            val navigateToMyExpense : LiveData<Boolean>
             get() = _navigateToMyExpense


    private val _todayExpense = MutableLiveData<String>()
    val todayExpense : LiveData<String>
        get() = _todayExpense

    private val _weekExpense = MutableLiveData<String>()
    val weekExpense : LiveData<String>
        get() = _weekExpense


    private val _monthExpense = MutableLiveData<String>()
    val monthExpense : LiveData<String>
        get() = _monthExpense

    init {
        checkIfDurationFinished()
        getTodayExpenses()
        getWeekExpenses()
        getMonthExpenses()
    }



    fun getTodayExpenses(){
        viewModelScope.launch {
                database.retrieveTodayExpense(getCurrencyFromSettings().toString()).collect {
                    _todayExpense.value = it
                }
        }
    }

    fun getWeekExpenses(){
        viewModelScope.launch {
            database.retrieveWeekExpense(getCurrencyFromSettings().toString()).collect {
                _weekExpense.value = it
            }
        }
    }

    fun getMonthExpenses(){
        viewModelScope.launch {
            database.retrieveMonthExpense(getCurrencyFromSettings().toString()).collect {
                _monthExpense.value = it
            }
        }
    }



    fun onFabClicked(){
       _navigateToMyExpense.value = true
    }


    fun onNavigatedToMyExpense(){
        _navigateToMyExpense.value = false
    }



    fun clearPrefs(){
        PrefManager.clear(application)
    }


    fun checkIfDurationFinished(){
        //today
        val savedCurrentDate = PrefManager.getCurrentDate(context)

        //week
        val weekDates = getStartAndEndOfTheWeek().split("*")
        val endOfWeek = weekDates[1]


        //month
        val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
        val endOfMonth = monthDates[1]

        //compare dates
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val savedDate = sdf.parse(savedCurrentDate)
            val currentDate = sdf.parse(getCurrentDate())
            val endOfTheWeek = sdf.parse(endOfWeek)
            val endOfTheMonth = sdf.parse(endOfMonth)

        if (currentDate.compareTo(savedDate) > 0){
            viewModelScope.launch {
                database.updateTodayExpenses(toBigDecimal("0"), getCurrencyFromSettings().toString())
                PrefManager.saveCurrentDate(context, getCurrentDate())
            }
        }

        if (currentDate.compareTo(endOfTheWeek) > 0){
            viewModelScope.launch {
                database.updateWeekExpenses(toBigDecimal("0"), getCurrencyFromSettings().toString())
            }
        }

        if(currentDate.compareTo(endOfTheMonth) > 0){
            viewModelScope.launch {
                database.updateMonthExpenses(toBigDecimal("0"), getCurrencyFromSettings().toString())
            }
        }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}