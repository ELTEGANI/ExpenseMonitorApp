package com.expensemoitor.expensemonitor.todayexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import com.expensemoitor.expensemonitor.database.UserExpenses
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse
import com.expensemoitor.expensemonitor.network.DurationTag
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.PrefManager.Companion.getCurrentDate
import com.expensemoitor.expensemonitor.utilites.ProgressStatus
import com.expensemoitor.expensemonitor.utilites.sumationOfAmount
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.math.BigDecimal


class TodayExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {

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


    init {
        getTodayExpense("today")
    }


      private fun getTodayExpense(duration:String) {
         viewModelScope.launch {
             val durationTag = PrefManager.getCurrency(application)?.let {
              DurationTag(duration,it,getCurrentDate(application),"")
          }
          val getResponse = durationTag?.let {
              ApiFactory.GET_DURATION_EXPNSES_SERVICE.getdurationExpensesAsync(it)
          }
             try {
                 try {
                     _status.value = ProgressStatus.LOADING
                     val getExpensesResponseList = getResponse?.await()
                     _status.value = ProgressStatus.DONE
                     if (getExpensesResponseList?.size != 0) {
                         _expensesProperties.value = getExpensesResponseList
                         if (database.checkCurrencyExistence(PrefManager.getCurrency(application).toString()) ==null) {
                             database.insertExpense(
                                 UserExpenses(
                                     todayExpenses = sumationOfAmount(getExpensesResponseList)
                                     , weekExpenses = BigDecimal.ZERO
                                     , monthExpenses = BigDecimal.ZERO
                                     , currency = PrefManager.getCurrency(application).toString()
                                 )
                             )
                         } else {
                             database.updateTodayExpenses(
                                 sumationOfAmount(getExpensesResponseList),
                                 PrefManager.getCurrency(application).toString()
                             )
                         }
                     } else {
                         PrefManager.getCurrency(application)?.let {
                             database.updateTodayExpenses(BigDecimal.ZERO,
                                 it
                             )
                         }
                         noExpeneseFound.value = context?.getString(R.string.no_daily_expenses)
                     }
                 } catch (t: Throwable) {
                     _status.value = ProgressStatus.ERROR
                     _expensesProperties.value = ArrayList()
                     noExpeneseFound.value = context?.getString(R.string.weak_internet_connection)
                     Log.d("Throwable", t.toString())
                 }
             } catch (http: HttpException){
                 Log.d("http", http.message())
                 http.code()
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