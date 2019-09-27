package com.expensemoitor.expensemonitor.monthexpense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.network.GetExpensesResponse
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


    private val _expensesProperties = MutableLiveData<List<GetExpensesResponse>>()
    val expensesProperties: LiveData<List<GetExpensesResponse>>
        get() = _expensesProperties



    init {
        getMonthExpense("month")
    }


    private fun getMonthExpense(duration:String) {
        coroutineScope.launch {
            val durationTag = DurationTag(duration)
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
