package com.expensemoitor.expensemonitor.monthexpense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.network.ExpensesResponse
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.getTheStartAndTheEndOfTheMonth
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MonthExpenseViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob+ Dispatchers.Main)

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
        coroutineScope.launch {
            val durationTag = DurationTag(duration,monthDates[0],monthDates[1])
            val getResponse = ApiFactory.GET_EXPNSES_BASED_ON_DURATION_SERVICE.getExpensesBasedOnDuration(durationTag)
            try {
                _status.value = progressStatus.LOADING
                val getExpensesResponseList = getResponse.await()
                _status.value = progressStatus.DONE
                _expensesProperties.value = getExpensesResponseList
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
