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
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.Converter.Companion.toBigDecimal
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MonthExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {


    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status


    private val _expensesProperties = MutableLiveData<List<DurationExpenseResponse>>()
    val expensesProperties: LiveData<List<DurationExpenseResponse>>
        get() = _expensesProperties

    private val _navigateToSelectedExpense = MutableLiveData<DurationExpenseResponse>()
    val navigateToSelectedExpense :LiveData<DurationExpenseResponse>
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
                ApiFactory.GET_DURATION_EXPNSES_SERVICE.getdurationExpenses(it)
            }
            try {
                _status.value = progressStatus.LOADING
                val getExpensesResponseList = getResponse?.await()
                _status.value = progressStatus.DONE
                _expensesProperties.value = getExpensesResponseList
                //TODO recheck validation and messgaes
                if(database.checkCurrencyExistence(getCurrencyFromSettings().toString()) == null){
                    UserExpenses(
                        todayExpenses   = BigDecimal.ZERO
                        ,weekExpenses   = BigDecimal.ZERO
                        ,monthExpenses  = sumationOfAmount(getExpensesResponseList)
                        ,currency = getCurrencyFromSettings().toString()
                    )
                }else{
                    database.updateMonthExpenses(sumationOfAmount(getExpensesResponseList),getCurrencyFromSettings().toString())
                }
            }catch (t:Throwable){
                _status.value = progressStatus.ERROR
                _expensesProperties.value = ArrayList()
                Log.d("getExpensesResponseList",t.toString())
            }
        }
    }

    fun displaySelectedExpense(durationExpenseResponse: DurationExpenseResponse){
        _navigateToSelectedExpense.value = durationExpenseResponse
    }


    fun displaySelectedExpenseCompleted(){
        _navigateToSelectedExpense.value = null
    }


}
