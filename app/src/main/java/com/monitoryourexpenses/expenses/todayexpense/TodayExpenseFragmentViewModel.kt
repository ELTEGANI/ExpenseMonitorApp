package com.monitoryourexpenses.expenses.todayexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class TodayExpenseFragmentViewModel @ViewModelInject constructor(var localRepository: LocalRepository,
    var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {

    var selectedExpense:MutableLiveData<List<Expenses>> = MutableLiveData<List<Expenses>>()

    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense: LiveData<Expenses>
        get() = _navigateToSelectedExpense

    init {
      viewModelScope.launch(Dispatchers.IO) {
          localRepository.getTodayExpenses().collect {todayExpenses->
              selectedExpense.postValue(todayExpenses)
          }
      }
    }

    fun getTodayExpenses() = selectedExpense

    fun displaySelectedExpense(expense: Expenses) {
        _navigateToSelectedExpense.value = expense
    }

    fun displaySelectedExpenseCompleted() {
        _navigateToSelectedExpense.value = null
    }
}
