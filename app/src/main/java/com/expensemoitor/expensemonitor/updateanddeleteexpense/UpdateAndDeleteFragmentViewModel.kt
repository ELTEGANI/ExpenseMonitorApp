package com.expensemoitor.expensemonitor.updateanddeleteexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.ExpenseData
import com.expensemoitor.expensemonitor.network.ExpensesResponse
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.calculateAfterUpdateExpenses
import com.expensemoitor.expensemonitor.utilites.getCurrencyFromSettings
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.*
import retrofit2.HttpException

class UpdateAndDeleteFragmentViewModel(expensesResponse: ExpensesResponse, application: Application) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext

    private val _selectedExpense = MutableLiveData<ExpensesResponse>()
    val selectedExpenseMsg :LiveData<ExpensesResponse>
        get() = _selectedExpense

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob+ Dispatchers.Main)

    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status


    private val _msgError = MutableLiveData<String>()
    val msgError:LiveData<String>
        get() = _msgError


    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg

    init {
        _selectedExpense.value = expensesResponse
    }



    fun deleteExpense(expenseId:String,amount:Int){
        coroutineScope.launch {
            val getDeleteExpenseResponse = ApiFactory.DELETE_EXPENSE.deleteExpense(expenseId)
            try {
                try {
                    _status.value = progressStatus.LOADING
                    val getResponse = getDeleteExpenseResponse.await()
                    if(getResponse.message.isNotEmpty()){
                        _msgError.value = "Expense Deleted Successfully"
                            val todayAmount = PrefManager.getTodayExpenses(application)
                            val weekAmount = PrefManager.getWeeKExpenses(application)
                            val monthAmount = PrefManager.getMonthExpenses(application)
                            //minus amount from duration
                        if (todayAmount != null && todayAmount > 0) {
                                PrefManager.saveUpdatedTodayExpense(application,todayAmount?.minus(amount))
                        }

                        if (weekAmount != null && weekAmount > 0){
                            PrefManager.saveUpdatedWeekExpense(application,weekAmount?.minus(amount))
                        }

                        if (monthAmount != null && monthAmount > 0) {
                                PrefManager.saveUpdatedMonthExpense(application,monthAmount?.minus(amount))
                        }

                        Log.d("getResponse",getResponse.toString())
                        _status.value = progressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = progressStatus.ERROR
                    _msgError.value = "Poor Internet Connection"
                }
            }catch (httpException: HttpException){
                Log.d("httpException",httpException.toString())
            }
        }
    }



    fun updateExpense(oldAmount:String,expenseId:String,newAmount:String,description:String,date:String,category:String){
        if(newAmount.isEmpty() || description.isEmpty() || date.isEmpty() || category.isEmpty()){
            _msgError.value = "Please Fill Empty Field"
        }else{
            coroutineScope.launch {
                val expenseData = getCurrencyFromSettings()?.let {
                    ExpenseData(newAmount,description,date,
                        it,category)
                }
                val getUpdateExpenseResponse =
                    expenseData?.let { ApiFactory.UPDATE_EXPENSE.updateExpense(expenseId, it) }
                try {
                    try {
                        _status.value = progressStatus.LOADING
                        val getResponse = getUpdateExpenseResponse?.await()
                        if(getResponse?.message?.isNotEmpty()!!){
                            _msgError.value = "Expense Updated Successfully"
                            calculateAfterUpdateExpenses(oldAmount,newAmount)
                        }
                        Log.d("getResponse",getResponse.toString())
                        _status.value = progressStatus.DONE
                    }catch (t:Throwable){
                        _status.value = progressStatus.ERROR
                        _validationMsg.value = "Poor Internet Connection"
                    }
                }catch (httpException: HttpException){
                    Log.d("httpException",httpException.toString())
                }
            }
        }
    }

    fun onMsgErrorDisplayed(){
       _msgError.value = null
    }

    fun onValidationErrorDisplayed(){
        _validationMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
