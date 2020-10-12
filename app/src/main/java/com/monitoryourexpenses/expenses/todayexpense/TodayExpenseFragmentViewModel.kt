package com.monitoryourexpenses.expenses.todayexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences


class TodayExpenseFragmentViewModel @ViewModelInject constructor(
    localRepository: LocalRepository,
    var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {

    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense: LiveData<Expenses>
        get() = _navigateToSelectedExpense

    val todayExpenses = localRepository.getTodayExpenses(expenseMonitorSharedPreferences.getCurrentDate().toString(),
        expenseMonitorSharedPreferences.getCurrency().toString())

    fun displaySelectedExpense(expense: Expenses) {
        _navigateToSelectedExpense.value = expense
    }

    fun displaySelectedExpenseCompleted() {
        _navigateToSelectedExpense.value = null
    }
}
