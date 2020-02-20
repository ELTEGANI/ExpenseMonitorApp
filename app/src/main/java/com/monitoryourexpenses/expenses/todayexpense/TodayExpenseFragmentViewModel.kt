package com.monitoryourexpenses.expenses.todayexpense

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.network.Expense
import com.monitoryourexpenses.expenses.utilites.PrefManager
import kotlinx.coroutines.launch


class TodayExpenseFragmentViewModel(val database: ExpenseMonitorDao,val application: Application) : ViewModel() {

    private val localRepository = LocalRepository(database)


    private val _expensesProperties = MutableLiveData<List<Expenses>>()
    val expensesProperties:LiveData<List<Expenses>>
        get() = _expensesProperties


    private val _navigateToSelectedExpense = MutableLiveData<Expenses>()
    val navigateToSelectedExpense :LiveData<Expenses>
        get() = _navigateToSelectedExpense


    init {
        viewModelScope.launch {
            _expensesProperties.value = localRepository.getTodayExpenses(
                   PrefManager.getCurrentDate(application).toString()
                , PrefManager.getCurrency(application).toString())
        }
    }






    fun displaySelectedExpense(expense: Expenses){
        _navigateToSelectedExpense.value = expense
    }


    fun displaySelectedExpenseCompleted(){
        _navigateToSelectedExpense.value = null
    }



}