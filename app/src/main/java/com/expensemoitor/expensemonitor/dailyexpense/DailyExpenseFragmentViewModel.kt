package com.expensemoitor.expensemonitor.dailyexpense

import android.util.Log
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DailyExpenseFragmentViewModel : ViewModel() {


    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)


    init {
        getTodayExpense("today")
    }


    private fun getTodayExpense(duration:String){
        coroutineScope.launch {
          val durationTag = DurationTag(duration)
          val getResponse = ApiFactory.GET_EXPNSES_BASED_ON_DURATION_SERVICE.getExpensesBasedOnDuration(durationTag)
            try {
             val getExpensesResponse = getResponse.await()
                Log.d("res",getExpensesResponse.toString())
            }catch (t:Throwable){
                Log.d("res",t.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}