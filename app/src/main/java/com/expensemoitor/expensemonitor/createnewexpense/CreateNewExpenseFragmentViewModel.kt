package com.expensemoitor.expensemonitor.createnewexpense


import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.widget.AdapterView
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.ExpenseData
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.*
import retrofit2.HttpException


class CreateNewExpenseFragmentViewModel(var application: Application) : ViewModel() {



    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    var selectedCategoryItem = ""



    init {
        currentDate.value = getCurrentDate()
    }

    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status

    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg


    private val _responseMsg = MutableLiveData<String>()
    val responseMsg: LiveData<String>
        get() = _responseMsg



    fun onSelectExpenseFormOrCategoryItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            selectedCategoryItem = parent.selectedItem.toString()
    }



    fun createExpenseClick(){
        val expenseAmount = amount.value
        val expenseDescription = description.value

        if(expenseAmount == null || expenseDescription == null){
            _validationMsg.value = "Please fill empty field"
        }else if(selectedCategoryItem.equals(application.getString(R.string.SelectCategory))){
            _validationMsg.value = "Please select Expense Category"
        }else{
            currentDate.value?.let {
                createNewExpense(expenseAmount,expenseDescription,
                    it,selectedCategoryItem)
            }
        }
    }


    private var viewModelJob = Job()
    private val coroutineJob  = CoroutineScope(viewModelJob + Dispatchers.Main)

    private fun createNewExpense(amount:String,description:String,date:String,category:String){
           coroutineJob.launch {
            val expenseData = PrefManager.getCurrency(application)?.let {
                ExpenseData(amount,description,date,
                    it,category)
            }
            val getResponse = expenseData?.let {
                ApiFactory.CREATE_EXPENSE_SERVICE.createNewExpense(
                    it
                )
            }
            try {
                try{
                    _status.value = progressStatus.LOADING
                    val expenseResponse = getResponse?.await()
                    if (!expenseResponse?.message?.isEmpty()!!){
                        _responseMsg.value = "Expense Created Successfully"
                        calculateAfterCreateExpenses(amount)
                        _status.value = progressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = progressStatus.ERROR
                    _validationMsg.value = t.toString()
                }
                }catch (httpException: HttpException){
                   Log.d("httpException",httpException.toString())
                }
           }
    }




    fun onNoEmptyFields(){
        _validationMsg.value = null
    }

    fun onResponseMsgDisplayed(){
        _responseMsg.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}