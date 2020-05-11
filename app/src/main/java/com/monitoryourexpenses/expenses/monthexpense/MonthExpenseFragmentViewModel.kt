package com.monitoryourexpenses.expenses.monthexpense

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.utilites.PrefManager


class MonthExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {

    private val localRepository = LocalRepository(database)


    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense :LiveData<Expenses>
        get() = _navigateToSelectedExpense

    val monthExpenses = localRepository.getMonthExpenses(PrefManager.getStartOfTheMonth(application).toString(),PrefManager.getEndOfTheMonth(application).toString(),PrefManager.getCurrency(application).toString())

    fun displaySelectedExpense(expenses: Expenses){
        _navigateToSelectedExpense.value = expenses
    }


    fun displaySelectedExpenseCompleted(){
        _navigateToSelectedExpense.value = null
    }


}
