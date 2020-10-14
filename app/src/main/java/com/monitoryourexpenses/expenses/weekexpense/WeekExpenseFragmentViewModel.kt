package com.monitoryourexpenses.expenses.weekexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences

class WeekExpenseFragmentViewModel @ViewModelInject constructor(localRepository: LocalRepository,
         var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {


    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense: LiveData<Expenses>
        get() = _navigateToSelectedExpense

    val weekExpenses = localRepository.getWeekExpenses(expenseMonitorSharedPreferences.getStartOfTheWeek().toString(),expenseMonitorSharedPreferences.getEndOfTheWeek().toString(),expenseMonitorSharedPreferences.getCurrency().toString())

    fun displaySelectedExpense(expenses: Expenses) {
        _navigateToSelectedExpense.value = expenses
    }

    fun displaySelectedExpenseCompleted() {
        _navigateToSelectedExpense.value = null
    }
}
