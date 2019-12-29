package com.expensemoitor.expensemonitor.monthexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import com.expensemoitor.expensemonitor.database.UserExpenses
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.network.ExpensesResponse
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.Converter.Companion.toBigDecimal
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MonthExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {


    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status


    private val _expensesProperties = MutableLiveData<List<ExpensesResponse>>()
    val expensesProperties: LiveData<List<ExpensesResponse>>
        get() = _expensesProperties

    private val _navigateToSelectedExpense = MutableLiveData<ExpensesResponse>()
    val navigateToSelectedExpense :LiveData<ExpensesResponse>
        get() = _navigateToSelectedExpense


    init {
        getMonthExpense("month")
    }


    private fun getMonthExpense(duration:String) {
        val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
        viewModelScope.launch {
            val durationTag = getCurrencyFromSettings()?.let {
                DurationTag(duration,it,monthDates[0],monthDates[1])
            }
            val getResponse = durationTag?.let {
                ApiFactory.GET_EXPNSES_BASED_ON_DURATION_SERVICE.getExpensesBasedOnDuration(it)
            }
            try {
                _status.value = progressStatus.LOADING
                val getExpensesResponseList = getResponse?.await()
                _status.value = progressStatus.DONE
                _expensesProperties.value = getExpensesResponseList
                //TODO get amount of mpnth expenses from backend and recheck validation and messgaes
                if(database.checkCurrencyExistence(getCurrencyFromSettings().toString()) == null){
                    database.insertExpense(UserExpenses(
                        todayExpenses  = toBigDecimal("0")
                        ,weekExpenses   = toBigDecimal("0")
                        ,monthExpenses  = toBigDecimal("30")//TODO should be come from server
                        ,currency = getCurrencyFromSettings().toString()
                    ))
                }else{
                    database.updateMonthExpenses(toBigDecimal("300"),getCurrencyFromSettings().toString())
                }
                Log.d("getExpensesResponseList",getExpensesResponseList.toString())
            }catch (t:Throwable){
                _status.value = progressStatus.ERROR
                _expensesProperties.value = ArrayList()
                Log.d("getExpensesResponseList",t.toString())
            }
        }
    }

    fun displaySelectedExpense(expensesResponse: ExpensesResponse){
        _navigateToSelectedExpense.value = expensesResponse
    }


    fun displaySelectedExpenseCompleted(){
        _navigateToSelectedExpense.value = null
    }


}
