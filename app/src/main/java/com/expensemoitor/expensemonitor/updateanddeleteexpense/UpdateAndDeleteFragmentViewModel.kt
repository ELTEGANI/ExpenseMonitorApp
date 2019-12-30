package com.expensemoitor.expensemonitor.updateanddeleteexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse
import com.expensemoitor.expensemonitor.network.ExpenseData
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import com.expensemoitor.expensemonitor.utilites.getCurrencyFromSettings
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.math.BigDecimal

class UpdateAndDeleteFragmentViewModel(durationExpenseResponse: DurationExpenseResponse, application: Application) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext

    private val _selectedExpense = MutableLiveData<DurationExpenseResponse>()
    val selectedExpenseMsg :LiveData<DurationExpenseResponse>
        get() = _selectedExpense


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
        _selectedExpense.value = durationExpenseResponse
    }



    fun deleteExpense(expenseId:String,amount:Int){
        viewModelScope.launch {
            val getDeleteExpenseResponse = ApiFactory.DELETE_EXPENSE.deleteExpense(expenseId)
            try {
                try {
                    _status.value = progressStatus.LOADING
                    val getResponse = getDeleteExpenseResponse.await()
                    if(getResponse.message.isNotEmpty()){
                        _msgError.value = context?.getString(R.string.expense_deleted_successfuly)


                        Log.d("getResponse",getResponse.toString())
                        _status.value = progressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = progressStatus.ERROR
                    _msgError.value = context?.getString(R.string.weak_internet_connection)
                }
            }catch (httpException: HttpException){
                Log.d("httpException",httpException.toString())
            }
        }
    }



    fun updateExpense(oldAmount:BigDecimal,expenseId:String,newAmount:BigDecimal,description:String,date:String,category:String){
        if(description.isEmpty() || date.isEmpty() || category.isEmpty()){
            _msgError.value =  context?.getString(R.string.fill_empty)
        }else{
            viewModelScope.launch {
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
                            _msgError.value = context?.getString(R.string.expense_update_successfuly)
                        }
                        Log.d("getResponse",getResponse.toString())
                        _status.value = progressStatus.DONE
                    }catch (t:Throwable){
                        _status.value = progressStatus.ERROR
                        _validationMsg.value = context?.getString(R.string.weak_internet_connection)
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

}
