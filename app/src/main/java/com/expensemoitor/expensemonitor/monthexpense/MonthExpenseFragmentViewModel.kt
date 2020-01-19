package com.expensemoitor.expensemonitor.monthexpense

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

class MonthExpenseFragmentViewModel(val database: ExpenseMonitorDao, val application: Application) : ViewModel() {


    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
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
            val durationTag = PrefManager.getCurrencyFromSettings()?.let {
                DurationTag(duration,it,PrefManager.getStartOfTheMonth(application),
                    PrefManager.getEndOfTheMonth(application))
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
                    if(database.checkCurrencyExistence(PrefManager.getCurrencyFromSettings().toString()) == null){
                        database.insertExpense(UserExpenses(
                            todayExpenses   = BigDecimal.ZERO
                            ,weekExpenses   = BigDecimal.ZERO
                            ,monthExpenses  = sumationOfAmount(getExpensesResponseList)
                            ,currency = PrefManager.getCurrencyFromSettings().toString()
                        ))
                    }else{
                        database.updateMonthExpenses(sumationOfAmount(getExpensesResponseList),PrefManager.getCurrencyFromSettings().toString())
                    }
                }else{
                    PrefManager.getCurrencyFromSettings()?.let {
                        database.updateMonthExpenses(BigDecimal.ZERO,
                            it
                        )
                    }
                    noExpeneseFound.value = context?.getString(R.string.no_monthly_expenses)
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
