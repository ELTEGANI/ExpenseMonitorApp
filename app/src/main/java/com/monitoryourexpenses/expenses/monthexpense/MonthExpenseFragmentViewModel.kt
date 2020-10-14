package com.monitoryourexpenses.expenses.monthexpense

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences
import javax.inject.Inject

class MonthExpenseFragmentViewModel @ViewModelInject constructor(var expenseMonitorSharedPreferences: ExpenseMonitorSharedPreferences,
         localRepository: LocalRepository) : ViewModel() {


    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense: LiveData<Expenses>
        get() = _navigateToSelectedExpense

    val monthExpenses = localRepository.getMonthExpenses(expenseMonitorSharedPreferences.getStartOfTheMonth().toString(),expenseMonitorSharedPreferences.getEndOfTheMonth().toString(),
        expenseMonitorSharedPreferences.getCurrency().toString())

    fun displaySelectedExpense(expenses: Expenses) {
        _navigateToSelectedExpense.value = expenses
    }

    fun displaySelectedExpenseCompleted() {
        _navigateToSelectedExpense.value = null
    }
}
