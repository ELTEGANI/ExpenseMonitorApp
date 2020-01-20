package com.expensemoitor.expensemonitor.weekexpense

import android.app.Application
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
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.launch
import java.math.BigDecimal

class WeekExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {


    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
        get() = _status

    private val _expensesProperties = MutableLiveData<List<DurationExpenseResponse>>()
    val expensesProperties:LiveData<List<DurationExpenseResponse>>
        get() = _expensesProperties

    private val _navigateToSelectedExpense = MutableLiveData<DurationExpenseResponse>()
    val navigateToSelectedExpense :LiveData<DurationExpenseResponse>
        get() = _navigateToSelectedExpense

    val noExpeneseFound = MutableLiveData<String>()


    init {
        getWeekExpense("week")
    }


    private fun getWeekExpense(duration:String) {

        viewModelScope.launch {
            val durationTag = PrefManager.getCurrency(application)?.let {
                DurationTag(duration,it,PrefManager.getStartOfTheWeek(application),
                    PrefManager.getEndOfTheWeek(application))
            }
            val getResponse = durationTag?.let {
                ApiFactory.GET_DURATION_EXPNSES_SERVICE.getdurationExpensesAsync(it)
            }
            try {
                _status.value = ProgressStatus.LOADING
                val getExpensesResponseList = getResponse?.await()
                _status.value = ProgressStatus.DONE
                if(getExpensesResponseList?.size != 0){
                    _expensesProperties.value = getExpensesResponseList
                    if(database.checkCurrencyExistence(PrefManager.getCurrency(application).toString()) == null){
                        database.insertExpense(UserExpenses(
                            todayExpenses   = BigDecimal.ZERO
                            ,weekExpenses   = sumationOfAmount(getExpensesResponseList)
                            ,monthExpenses  = BigDecimal.ZERO
                            ,currency = PrefManager.getCurrency(application).toString()
                        ))
                    }else{
                        database.updateWeekExpenses(sumationOfAmount(getExpensesResponseList),PrefManager.getCurrency(application).toString())
                    }
                }else{
                    PrefManager.getCurrency(application)?.let {
                        database.updateWeekExpenses(BigDecimal.ZERO,
                            it
                        )
                    }
                    noExpeneseFound.value = context?.getString(R.string.no_weekly_expenses)
                }

            }catch (t:Throwable){
                _status.value = ProgressStatus.ERROR
                _expensesProperties.value = ArrayList()
                noExpeneseFound.value = context?.getString(R.string.weak_internet_connection)
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