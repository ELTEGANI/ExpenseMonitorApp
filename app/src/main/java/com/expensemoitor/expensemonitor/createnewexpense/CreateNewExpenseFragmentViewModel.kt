package com.expensemoitor.expensemonitor.createnewexpense


import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.utilites.getCurrentDate
import android.widget.AdapterView
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.ExpenseData
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.progressStatus
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
            val expenseData = ExpenseData(amount,description,date,category)
            val getResponse = ApiFactory.CREATE_EXPENSE_SERVICE.createNewExpense(expenseData)
            try {
                try{
                    _status.value = progressStatus.LOADING
                    val expensResponse = getResponse.await()
                    if (expensResponse.message.isNotEmpty()){
                        _responseMsg.value = "Expense Created Successfully"
                            val newTodayExpense = PrefManager.getTodayExpenses(context)?.plus(amount.toInt())
                            val newWeekExpense = PrefManager.getWeeKExpenses(context)?.plus(amount.toInt())
                            val newMonthExpense = PrefManager.getMonthExpenses(context)?.plus(amount.toInt())
                            PrefManager.saveUpdatedTodayExpense(application,newTodayExpense)
                            PrefManager.saveUpdatedWeekExpense(application,newWeekExpense)
                            PrefManager.saveUpdatedMonthExpense(application,newMonthExpense)
                        _status.value = progressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = progressStatus.ERROR
                    _responseMsg.value = "Poor Internet Connection"
                }
                }catch (httpException: HttpException){
                   Log.d("httpException",httpException.toString())
                }
           }
    }




    fun onNoEmptyFields(){
        _validationMsg.value = null
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}