package com.expensemoitor.expensemonitor.todayexpense

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
import com.expensemoitor.expensemonitor.utilites.getCurrencyFromSettings
import com.expensemoitor.expensemonitor.utilites.progressStatus
import com.expensemoitor.expensemonitor.utilites.sumationOfAmount
import kotlinx.coroutines.*
import java.math.BigDecimal


class TodayExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {




    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status


    private val _expensesProperties = MutableLiveData<List<DurationExpenseResponse>>()
    val expensesProperties:LiveData<List<DurationExpenseResponse>>
       get() = _expensesProperties

    private val _navigateToSelectedExpense = MutableLiveData<DurationExpenseResponse>()
    val navigateToSelectedExpense :LiveData<DurationExpenseResponse>
        get() = _navigateToSelectedExpense


    init {
        getTodayExpense("today")
    }


     private fun getTodayExpense(duration:String) {
         viewModelScope.launch {
             val durationTag = getCurrencyFromSettings()?.let {
              DurationTag(duration,
                  it,"","")
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
                            todayExpenses  = sumationOfAmount(getExpensesResponseList)
                            ,weekExpenses   = "0".toBigDecimal()
                            ,monthExpenses  = "0".toBigDecimal()
                            ,currency = getCurrencyFromSettings().toString()
                        )
                }else{
                    database.updateTodayExpenses(sumationOfAmount(getExpensesResponseList),getCurrencyFromSettings().toString())
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