package com.expensemoitor.expensemonitor.todayexpense

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.network.ExpensesResponse
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class TodayExpenseFragmentViewModel : ViewModel() {


    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)

    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status


    private val _expensesProperties = MutableLiveData<List<ExpensesResponse>>()
    val expensesProperties:LiveData<List<ExpensesResponse>>
       get() = _expensesProperties


    fun onSelectExpenseFormOrCategoryItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

    }



    init {
        getTodayExpense("today")
    }


    private fun getTodayExpense(duration:String) {
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