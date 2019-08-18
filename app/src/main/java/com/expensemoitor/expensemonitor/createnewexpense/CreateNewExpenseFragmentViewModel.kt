package com.expensemoitor.expensemonitor.createnewexpense


import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.utilites.displayCurrentDate
import android.widget.AdapterView
import android.widget.Spinner
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.network.ExpenseData
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class CreateNewExpenseFragmentViewModel(var application: Application) : ViewModel() {



    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    var selectedFormsItem = ""
    var selectedCategoryItem = ""



    init {
        currentDate.value = displayCurrentDate()
    }

    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status

    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg

    private val _navigateToMyExpenseFragment = MutableLiveData<Boolean>()
    val navigateToMyExpenseFragment : LiveData<Boolean>
        get() = _navigateToMyExpenseFragment


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
            val getResponse = ApiFactory.CREATE_EXPENSE_URLS.createNewExpense(expenseData)
            try {
                _status.value = progressStatus.LOADING
                val expensResponse = getResponse.await()
                PrefManager.saveUpdatedExpense(application,expensResponse.Expense)
                _navigateToMyExpenseFragment.value = true
                _status.value = progressStatus.DONE
            }catch (t:Throwable){
                _status.value = progressStatus.ERROR
                _navigateToMyExpenseFragment.value = false
            }
           }
    }




    fun onNoEmptyFields(){
        _validationMsg.value = ""
    }



    fun onNavigateToMyExpnse(){
        _navigateToMyExpenseFragment.value = false
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}