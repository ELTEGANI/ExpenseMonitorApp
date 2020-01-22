package com.monitoryourexpenses.expenses.updateanddeleteexpense

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.network.ApiFactory
import com.monitoryourexpenses.expenses.network.DurationExpenseResponse
import com.monitoryourexpenses.expenses.network.ExpenseData
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.monitoryourexpenses.expenses.utilites.PrefManager
import com.monitoryourexpenses.expenses.utilites.ProgressStatus
import kotlinx.coroutines.*
import retrofit2.HttpException

class UpdateAndDeleteFragmentViewModel(durationExpenseResponse: DurationExpenseResponse, application: Application) : AndroidViewModel(application) {

    private val application = getApplication<Application>().applicationContext

    private val _selectedExpense = MutableLiveData<DurationExpenseResponse>()
    val selectedExpenseMsg :LiveData<DurationExpenseResponse>
        get() = _selectedExpense


    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
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



    fun deleteExpense(expenseId:String){
        viewModelScope.launch {
            val getDeleteExpenseResponse = ApiFactory.DELETE_EXPENSE.deleteExpenseAsync(expenseId)
            try {
                try {
                    _status.value = ProgressStatus.LOADING
                    val getResponse = getDeleteExpenseResponse.await()
                    if(getResponse.message.isNotEmpty()){
                        _msgError.value = context?.getString(R.string.expense_deleted_successfuly)
                        Log.d("getResponse",getResponse.toString())
                        _status.value = ProgressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = ProgressStatus.ERROR
                    _msgError.value = context?.getString(R.string.weak_internet_connection)
                }
            }catch (httpException: HttpException){
                Log.d("httpException",httpException.toString())
            }
        }
    }



    fun updateExpense(expenseId:String,newAmount:String,description:String,date:String,category:String){
        if(description.isEmpty() || date.isEmpty() || category.isEmpty()){
            _msgError.value =  context?.getString(R.string.fill_empty)
        }else if(category == application.getString(R.string.SelectCategory)){
            _validationMsg.value = context?.getString(R.string.select_category)
        }else{
            viewModelScope.launch {
                val expenseData = PrefManager.getCurrency(application)?.let {
                    ExpenseData(newAmount,description,date,
                        it,category)
                }
                val getUpdateExpenseResponse =
                    expenseData?.let { ApiFactory.UPDATE_EXPENSE.updateExpenseAsync(expenseId, it) }
                try {
                    try {
                        _status.value = ProgressStatus.LOADING
                        val getResponse = getUpdateExpenseResponse?.await()
                        if (getResponse != null) {
                            if(getResponse.message.isNotEmpty()){
                                _msgError.value = context?.getString(R.string.expense_update_successfuly)
                            }
                        }
                        _status.value = ProgressStatus.DONE
                    }catch (t:Throwable){
                        _status.value = ProgressStatus.ERROR
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
