package com.monitoryourexpenses.expenses.myexpenses

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.database.amountOfAllCurrencies
import com.monitoryourexpenses.expenses.utilites.*
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*


class MyExpenseFragmentViewModel(val database:ExpenseMonitorDao, application: Application) : AndroidViewModel(application) {

    private val localRepository = LocalRepository(database)

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

    val sumationOfCurrencies: LiveData<List<amountOfAllCurrencies>> =
        localRepository.selectSumationOfCurrencies()

    init {
        viewModelScope.launch {
            checkIfDurationFinished()
            getTodayExpenses()
            getWeekExpenses()
            getMonthExpenses()
        }
    }


    private fun getTodayExpenses(){
        viewModelScope.launch {
            localRepository.getSumationOfTodayExpenses(PrefManager.getCurrentDate(application).toString(),PrefManager.getCurrency(application).toString()).collect {
                        _todayExpense.value = expenseAmountFormatter(it)
                }
        }
    }

    private fun getWeekExpenses(){
        viewModelScope.launch {
            localRepository.getSumationOfWeekExpenses(PrefManager.getStartOfTheWeek(application).toString(),
                PrefManager.getEndOfTheWeek(application).toString(),PrefManager.getCurrency(application).toString()).collect {
                    _weekExpense.value = expenseAmountFormatter(it)
            }
        }
    }

    private fun getMonthExpenses(){
        viewModelScope.launch {
            localRepository.getSumationOfMonthExpenses(PrefManager.getStartOfTheMonth(application).toString(),
                PrefManager.getEndOfTheMonth(application).toString(),PrefManager.getCurrency(application).toString()).collect {
                    _monthExpense.value = expenseAmountFormatter(it)
            }
        }
    }


    fun onFabClicked(){
       _navigateToMyExpense.value = true
    }


    fun onNavigatedToMyExpense(){
        _navigateToMyExpense.value = false
    }



    private fun checkIfDurationFinished(){
        //today
        val savedCurrentDate = PrefManager.getCurrentDate(context)

        //week
        val endOfWeek = PrefManager.getEndOfTheWeek(context)


        //month
        val endOfMonth = PrefManager.getEndOfTheMonth(context)

        //compare dates
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val savedDate = sdf.parse(savedCurrentDate)
            val currentDate = sdf.parse(LocalDate.now().toString())
            val endOfTheWeek = sdf.parse(endOfWeek)
            val endOfTheMonth = sdf.parse(endOfMonth)



        if (currentDate > savedDate){
            viewModelScope.launch {
                PrefManager.saveCurrentDate(context,LocalDate.now().toString())
            }
        }

        if (currentDate > endOfTheWeek){
            viewModelScope.launch {
                PrefManager.saveStartOfTheWeek(context,LocalDate.now().toString())
                PrefManager.saveEndOfTheWeek(application,LocalDate.now().plusDays(7).toString())
            }
        }

        if(currentDate > endOfTheMonth){
            viewModelScope.launch {
               localRepository.clearMonthExpenses(PrefManager.getStartOfTheMonth(application).toString(),PrefManager.getEndOfTheMonth(application).toString())
                PrefManager.saveStartOfTheMonth(application,LocalDate.now().toString())
                PrefManager.saveEndOfTheMonth(application,LocalDate.now().plusMonths(1).toString())
            }
        }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}

