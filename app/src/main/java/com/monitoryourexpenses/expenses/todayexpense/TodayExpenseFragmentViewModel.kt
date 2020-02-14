package com.monitoryourexpenses.expenses.todayexpense

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.network.DurationExpenseResponse
import com.monitoryourexpenses.expenses.utilites.ProgressStatus


class TodayExpenseFragmentViewModel(val database: ExpenseMonitorDao,val application: Application) : ViewModel() {

    private val localRepository = LocalRepository(database)

    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
        get() = _status

    val noExpeneseFound = MutableLiveData<String>()

    private val _expensesProperties = MutableLiveData<List<DurationExpenseResponse>>()
    val expensesProperties:LiveData<List<DurationExpenseResponse>>
       get() = _expensesProperties

    private val _navigateToSelectedExpense = MutableLiveData<DurationExpenseResponse>()
    val navigateToSelectedExpense :LiveData<DurationExpenseResponse>
        get() = _navigateToSelectedExpense


//    init {
//        getTodayExpense("today")
//    }


//      private fun getTodayExpense(duration:String) {
//         viewModelScope.launch {
//             val durationTag = PrefManager.getCurrency(application)?.let {
//              DurationTag(duration,it,PrefManager.getCurrentDate(application),"")
//          }
//          val getResponse = durationTag?.let {
//              ApiFactory.GET_DURATION_EXPNSES_SERVICE.getdurationExpensesAsync(it)
//          }
//             try {
//                 try {
//                     _status.value = ProgressStatus.LOADING
//                     val getExpensesResponseList = getResponse?.await()
//                     _status.value = ProgressStatus.DONE
//                     if (getExpensesResponseList?.size != 0) {
//
//                     } else {
//                         noExpeneseFound.value = context?.getString(R.string.no_daily_expenses)
//                     }
//                 } catch (t: Throwable) {
//                     _status.value = ProgressStatus.ERROR
//                     _expensesProperties.value = ArrayList()
//                     noExpeneseFound.value = context?.getString(R.string.weak_internet_connection)
//                     Log.d("Throwable", t.toString())
//                 }
//             } catch (http: HttpException){
//                 Log.d("http", http.message())
//             }
//        }
//    }



    fun displaySelectedExpense(durationExpenseResponse: DurationExpenseResponse){
        _navigateToSelectedExpense.value = durationExpenseResponse
    }


    fun displaySelectedExpenseCompleted(){
        _navigateToSelectedExpense.value = null
    }



}