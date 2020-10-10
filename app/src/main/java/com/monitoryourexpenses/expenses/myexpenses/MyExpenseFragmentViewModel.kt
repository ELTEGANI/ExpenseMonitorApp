package com.monitoryourexpenses.expenses.myexpenses

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.monitoryourexpenses.expenses.database.AllCategories
import com.monitoryourexpenses.expenses.database.AllCurrencies
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import com.monitoryourexpenses.expenses.utilites.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.threeten.bp.LocalDate

class MyExpenseFragmentViewModel @ViewModelInject constructor(var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences,
                                                                  private var localRepository: LocalRepository,
                                                                  var utilitesFunctions:UtilitesFunctions) : ViewModel() {

    private val _navigateToMyExpense = MutableLiveData<Boolean>()
    val navigateToMyExpense: LiveData<Boolean>
    get() = _navigateToMyExpense

    private val _todayExpense = MutableLiveData<String>()
    val todayExpense: LiveData<String>
        get() = _todayExpense

    private val _weekExpense = MutableLiveData<String>()
    val weekExpense: LiveData<String>
        get() = _weekExpense

    private val _monthExpense = MutableLiveData<String>()
    val monthExpense: LiveData<String>
        get() = _monthExpense

    val sumationOfCurrencies: LiveData<List<AllCurrencies>> =
        localRepository.selectSumationOfCurrencies()

    val sumationOfCategories: LiveData<List<AllCategories>>? =
        expenseMonitorSharedPreferences.getEndOfTheMonth()?.let { startEndTheMonth ->
            expenseMonitorSharedPreferences.getStartOfTheMonth()?.let { startOfTheMonth ->
                expenseMonitorSharedPreferences.getCurrency()?.let { currency ->
                  localRepository.selectSumationOfCategories(
                      startOfTheMonth,
                      startEndTheMonth,
                      currency
                      )
              }
            }
        }

    init {
        viewModelScope.launch {
            checkIfDurationFinished()
            getTodayExpenses()
            getWeekExpenses()
            getMonthExpenses()
        }
    }

    private fun getTodayExpenses() {
        viewModelScope.launch {
            localRepository.getSumationOfTodayExpenses(expenseMonitorSharedPreferences.getCurrentDate().toString(),
                expenseMonitorSharedPreferences.getCurrency().toString()).collect {
                        _todayExpense.value = utilitesFunctions.expenseAmountFormatter(it)
                }
        }
    }

    private fun getWeekExpenses() {
        viewModelScope.launch {
            localRepository.getSumationOfWeekExpenses(expenseMonitorSharedPreferences.getStartOfTheWeek().toString(),
                expenseMonitorSharedPreferences.getEndOfTheWeek().toString(),expenseMonitorSharedPreferences.getCurrency().toString()).collect {
                    _weekExpense.value = utilitesFunctions.expenseAmountFormatter(it)
            }
        }
    }

    private fun getMonthExpenses() {
        viewModelScope.launch {
            localRepository.getSumationOfMonthExpenses(expenseMonitorSharedPreferences.getStartOfTheMonth().toString(),
                expenseMonitorSharedPreferences.getEndOfTheMonth().toString(),expenseMonitorSharedPreferences.getCurrency().toString()).collect {
                    _monthExpense.value = utilitesFunctions.expenseAmountFormatter(it)
            }
        }
    }

    fun onFabClicked() {
       _navigateToMyExpense.value = true
    }

    fun onNavigatedToMyExpense() {
        _navigateToMyExpense.value = false
    }

    private fun checkIfDurationFinished() {
        // today
        val savedCurrentDate = expenseMonitorSharedPreferences.getCurrentDate()

        // week
        val endOfWeek = expenseMonitorSharedPreferences.getEndOfTheWeek()

        // month
        val endOfMonth = expenseMonitorSharedPreferences.getEndOfTheMonth()

        // compare dates
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val savedDate = sdf.parse(savedCurrentDate)
            val currentDate = sdf.parse(LocalDate.now().toString())
            val endOfTheWeek = sdf.parse(endOfWeek)
            val endOfTheMonth = sdf.parse(endOfMonth)

        if (currentDate > savedDate) {
            viewModelScope.launch {
                expenseMonitorSharedPreferences.saveCurrentDate(LocalDate.now().toString())
            }
        }

        if (currentDate > endOfTheWeek) {
            viewModelScope.launch {
                expenseMonitorSharedPreferences.saveStartOfTheWeek(LocalDate.now().toString())
                expenseMonitorSharedPreferences.saveEndOfTheWeek(LocalDate.now().plusDays(7).toString())
            }
        }

        if (currentDate > endOfTheMonth) {
            viewModelScope.launch {
                expenseMonitorSharedPreferences.saveStartOfTheMonth(LocalDate.now().toString())
                expenseMonitorSharedPreferences.saveEndOfTheMonth(LocalDate.now().plusMonths(1).toString())
            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
