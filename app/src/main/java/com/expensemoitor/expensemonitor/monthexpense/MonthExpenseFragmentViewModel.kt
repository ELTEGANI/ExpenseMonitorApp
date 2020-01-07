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

    val noExpeneseFound = MutableLiveData<String>()


    init {
        getMonthExpense("month")
    }


    private fun getMonthExpense(duration:String) {
        viewModelScope.launch {
            val durationTag = getCurrencyFromSettings()?.let {
                DurationTag(duration,it,PrefManager.getStartOfTheMonth(application),
                    PrefManager.getEndOfTheMonth(application))
            }
            val getResponse = durationTag?.let {
                ApiFactory.GET_DURATION_EXPNSES_SERVICE.getdurationExpenses(it)
            }
            try {
                _status.value = progressStatus.LOADING
                val getExpensesResponseList = getResponse?.await()
                _status.value = progressStatus.DONE
                if(getExpensesResponseList?.size != 0){
                    _expensesProperties.value = getExpensesResponseList
                    if(database.checkCurrencyExistence(getCurrencyFromSettings().toString()) == null){
                        database.insertExpense(UserExpenses(
                            todayExpenses   = BigDecimal.ZERO
                            ,weekExpenses   = BigDecimal.ZERO
                            ,monthExpenses  = sumationOfAmount(getExpensesResponseList)
                            ,currency = getCurrencyFromSettings().toString()
                        ))
                    }else{
                        database.updateMonthExpenses(sumationOfAmount(getExpensesResponseList),getCurrencyFromSettings().toString())
                    }
                }else{
                    noExpeneseFound.value = "There Are No Monthly Expenses"
                }

            }catch (t:Throwable){
                _status.value = progressStatus.ERROR
                _expensesProperties.value = ArrayList()
                noExpeneseFound.value = "Please Check Internet Connection"
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
