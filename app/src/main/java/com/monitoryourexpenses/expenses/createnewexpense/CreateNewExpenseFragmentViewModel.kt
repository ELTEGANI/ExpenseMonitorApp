package com.monitoryourexpenses.expenses.createnewexpense


import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.widget.AdapterView
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.database.Expenses
import com.monitoryourexpenses.expenses.database.LocalRepository
import com.monitoryourexpenses.expenses.network.ApiFactory
import com.monitoryourexpenses.expenses.network.ExpenseData
import com.monitoryourexpenses.expenses.utilites.*
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import retrofit2.HttpException


class CreateNewExpenseFragmentViewModel(val database: ExpenseMonitorDao,var application: Application) : ViewModel() {


    private val localRepository = LocalRepository(database)

    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()
    private var selectedCategoryItem = ""



    init {
        currentDate.value = LocalDate.now().toString()
    }

    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
        get() = _status

    private val _validationMsg = MutableLiveData<String>()
    val validationMsg: LiveData<String>
        get() = _validationMsg


    private val _responseMsg = MutableLiveData<String>()
    val responseMsg: LiveData<String>
        get() = _responseMsg

    private val _exceedsMessage = MutableLiveData<String>()
    val exceedsMessage: LiveData<String>
        get() = _exceedsMessage



    fun onSelectExpenseFormOrCategoryItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedCategoryItem = parent.selectedItem.toString()
    }


    fun createExpenseClick(){
        val expenseAmount = amount.value
        val expenseDescription = description.value

        if(expenseAmount == null || expenseDescription == null){
            _validationMsg.value = context?.getString(R.string.fill_empty)
        }else if(selectedCategoryItem == application.getString(R.string.SelectCategory)){
            _validationMsg.value = context?.getString(R.string.select_category)
        }else{
            viewModelScope.launch {
                if (localRepository.sumationOfSpecifiedExpenses(PrefManager.getCurrency(application).toString())
                    == PreferenceManager.getDefaultSharedPreferences(application).getString("exceed_expense",null).toString()){
                    _exceedsMessage.value = PreferenceManager.getDefaultSharedPreferences(application).getString("exceed_expense",null)
                }else{
                    currentDate.value?.let {
                        createNewExpense(expenseAmount,expenseDescription,
                            it,selectedCategoryItem)
                    }
                }
            }
        }
    }

    private fun createNewExpense(amount:String,description:String,date:String,category:String){
        viewModelScope.launch {
            val expenseData = PrefManager.getCurrency(application)?.let {
                ExpenseData(amount,description,date,
                    it,category)
            }
            val getResponse = expenseData?.let {
                ApiFactory.CREATE_EXPENSE_SERVICE.createNewExpenseAsync(
                    it
                )
            }
            try {
                try{
                    _status.value = ProgressStatus.LOADING
                    val expenseResponse = getResponse?.await()
                    if (expenseResponse?.message != null){
                        expenseResponse.expense?.amount?.let {
                            Expenses(
                                id = expenseResponse.expense?.id.toString(),
                                amount = it,
                                description = expenseResponse.expense?.description.toString(),
                                expenseCategory = expenseResponse.expense?.expenseCategory.toString(),
                                currency = expenseResponse.expense?.currency.toString(),
                                date = expenseResponse.expense?.date.toString()
                            )
                        }?.let { localRepository.insertExpense(it) }
                        _responseMsg.value = context?.getString(R.string.expense_created_successfuly)
                        _status.value = ProgressStatus.DONE
                    }
                }catch (t:Throwable){
                    _status.value = ProgressStatus.ERROR
                    _validationMsg.value =context?.getString(R.string.weak_internet_connection)
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

    fun exceedMsgDisplayed(){
        _exceedsMessage.value = null
    }

}