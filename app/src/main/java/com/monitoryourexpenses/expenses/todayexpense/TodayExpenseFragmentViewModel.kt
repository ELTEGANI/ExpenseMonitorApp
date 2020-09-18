package com.monitoryourexpenses.expenses.todayexpense

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.prefs.ExpenseMonitorSharedPreferences

class TodayExpenseFragmentViewModel @ViewModelInject constructor(localRepository: LocalRepository,
sharedPreferences: ExpenseMonitorSharedPreferences) : ViewModel() {

    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense: LiveData<Expenses>
        get() = _navigateToSelectedExpense

    val todayExpenses = localRepository.getTodayExpenses(sharedPreferences.getCurrentDate().toString(),
        sharedPreferences.getCurrency().toString())

    fun displaySelectedExpense(expense: Expenses) {
        _navigateToSelectedExpense.value = expense
    }

    fun displaySelectedExpenseCompleted() {
        _navigateToSelectedExpense.value = null
    }
}
